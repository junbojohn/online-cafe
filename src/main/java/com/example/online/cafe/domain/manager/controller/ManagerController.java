package com.example.online.cafe.domain.manager.controller;

import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import com.example.online.cafe.domain.manager.service.ManagerService;
import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.dto.MenuManagerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*
관리자가 로그인하거나 혹은 새로운 관리자가 가입할 수 있게 해주는 html 페이지를 보여준다.(GET 요청)

관리자의 로그인, 로그아웃, 그리고 가입 기능 자체를 담당하는 POST 요청은 'AuthController' 에 있다.

그외에도 로그인한 관리자에게 새로운 상품 추가, 기존 상품 수정 및 삭제를 할 수 있게 해주는 html 페이지를 보여줌은 물론
그 기능들을 작업하는 POST 요청들도 이곳에 포함되어 있다.
*/

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;

    private final MenuRepository menuRepository;

    // 관리자가 로그인 할 수 있게 해주는 html 페이지를 보여준다.
    @GetMapping("/login")
    public String managerLogin() {
        return "managerLogin";
    }

    // 새로운 관리자로 가입 할 수 있게 해주는 html 페이지를 보여준다.
    @GetMapping("/signup")
    public String managerSignup() {
        return "managerSignup";
    }

    // 관리자에게 특정 기능을 이용할 수 있게 해주는 기능 목록 html 페이지를 보여준다.
    // 이곳에서 관리자는 로그아웃, 상품 등록, 상품 수정, 혹은 상품 삭제 기능을 선택할 수 있는 목록을 볼 수 있다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu")
    public String showManagerMenu(
            @RequestParam(defaultValue = "0")
            int page
    ) {
        return "managerMenu";
    }

    // 관리자에게 새로운 상품 데이터를 생성할 수 있게 해준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/add")
    public String addMenu() {
        return "addMenu";
    }

    // 관리자가 새로 만든 상품 데이터를 생성 및 DB 테이블에 삽입한다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @PostMapping("/menu/add/process")
    public String addMenuProcess(
            @RequestParam
            String menu_name,

            @RequestParam
            Integer price
    ) {
        System.out.println("Adding new menu...");

        // 입력한 상품 이름이 비어있는지, 그리고 가격의 값이 1 이하인지 확인한다.
        // 둘 중 하나라도 해당할 경우, 상품 추가 페이지로 리다이렉트 시킨다.
        if (menu_name.isBlank() || price < 1) {
            System.out.println("Either menu name is empty or the price is lower than 1.");
            return "redirect:/manager/menu/add";
        }

        // 기존에 있는 상품 데이터들을 불러온다.
        List<Menu> menuList = menuRepository.findAll();

        System.out.println("Checking menu duplication...");

        // 새로 생성하고자 하는 상품의 이름이 기존에 있던 상품들 중에 있는지 없는지 확인한다.
        // 만약 있다면, 상품 추가 페이지로 리다이렉트 시킨다.
        for (Menu comparingData : menuList) {
            //System.out.println("Checking menu: " + comparingData.getMenu_name());
            //System.out.println("same name?: " + comparingData.getMenu_name().equalsIgnoreCase(menu_name));

            if (comparingData.getMenuName().equalsIgnoreCase(menu_name)) {
                System.out.println("There already exists a menu named: " + menu_name);
                return "redirect:/manager/menu/add";
            }
        }

        // 생성하고자 하는 상품의 이름이 중복되지 않으면 새로운 상품 데이터를 생싱 및 삽입한다.
        managerService.addMenu(menu_name, price);

        // 신규 상품 데이터 등록 성공 후, 등록에 성공했다는 메세지를 출력하는 html 페이지를 보여준다.
        return "addSuccess";
    }



    // 밑에 있는 함수들은 관리자가 기존 상품 데이터들을 수정 및 삭제 할 수 있게 해주는 함수들이다.
    // 관리자에게 보여줄 상품 목록 데이터를 'ManagerService' 클래스를 이용해 'MenuManagerDto' 에서 가져와 반환한다.
    // 그럼 다음, 각 기능마다 가지고 있는 전용 html 페이지를 통해서 해당 기능들을 이용할 수 있게 한다.(GET 요청)
    // 전용 html 페이지 다음으로는 각 기능들의 작업을 수행하고 수행 이 후에는 작업이 성공적으로 끝났다는 별도의 html 페이지에서 메세지를 표기한다.(POST 요청)


    // 관리자에게 기존의 상품 데이터 목록을 보여줌으로서 특정 상품의 대해 수정 및 삭제 기능을 이용하게 해주는 링크를 보여준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/editAndDelete")
    public String editMenu(
            @RequestParam(defaultValue = "0")
            int page,

            //기본적으로 모든 상품이 50,000원을 넘기지 않는다는 하에 가정하여 기본값을 50000으로 한다. 이래야 기본으로 나오는 메뉴에 모든 상품이 나올 수 있다.
            @RequestParam(defaultValue = "50000")
            Integer price,

            Model model
    ) {
        MenuManagerDto<Menu> menuManagerDto = managerService.showManagerMenu(page, price);

        model.addAttribute("menuManagerDto", menuManagerDto);
        model.addAttribute("price", price);

        return "editAndDeleteMenu";
    }

    // 관리자에게 이미 존재하는 특정 상품의 데이터를 수정할 수 있게 해준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/editAndDelete/edit")
    public String editMenu(
            @RequestParam(required = true)
            Long menuId,

            Model model
    ) {
        Menu menuToEdit = managerService.showMenuDetails(menuId);

        model.addAttribute("menuToEdit", menuToEdit);
        model.addAttribute("menuId", menuId);

        return "editMenu";
    }

    // 관리자가 수정을 시도한 기존 상품 데이터의 수정 내용을 적용한다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @PostMapping("/menu/editAndDelete/edit/process")
    public String editMenuProcess(
            @RequestParam(required = true)
            Long menuId,

            @RequestParam
            String menu_name,

            @RequestParam
            Integer price
    ) {
        System.out.println("Editing original menu...");

        // 입력한 상품 이름이 비어있는지, 그리고 가격의 값이 1 이하인지 확인한다.
        // 둘 중 하나라도 해당할 경우, 기존의 상품 데이터 목록을 보여줌으로서 수정 및 삭제 버튼을 제공해주는 페이지로 리다이렉트 시킨다.
        if (menu_name.isBlank() || price < 1) {
            System.out.println("Either menu name is empty or the price is lower than 1.");
            return "redirect:/manager/menu/editAndDelete";
        }

        // 기존에 있는 상품 데이터들을 불러온다.
        List<Menu> menuList = menuRepository.findAll();

        System.out.println("Checking menu duplication...");

        // 수정하고자 하는 상품의 이름이 기존에 있던 상품들 중에 있는지 없는지 확인한다.
        // 만약 있다면, 기존의 상품 데이터 목록을 보여줌으로서 수정 및 삭제 버튼을 제공해주는 페이지로 리다이렉트 시킨다.
        for (Menu comparingData : menuList) {
            //System.out.println("Checking menu: " + comparingData.getMenu_name());
            //System.out.println("same name?: " + comparingData.getMenu_name().equalsIgnoreCase(menu_name));

            if (comparingData.getMenuName().equalsIgnoreCase(menu_name)) {
                System.out.println("There already exists a menu named: " + menu_name);
                return "redirect:/manager/menu/editAndDelete";
            }
        }

        // 수정하려는 상품의 이름이 기존 상품들 중에 없다는 것이 확인되면 상품 데이터의 수정 내용을 적용한다.
        managerService.editMenu(menuId, menu_name, price);

        // 상품 데이터 수정 성공 후, 수정에 성공했다는 메세지를 출력하는 html 페이지를 보여준다.
        return "editSuccess";
    }

    // 관리자에게 기존의 상품 데이터를 삭제할 수 있게 해준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/editAndDelete/delete")
    public String deleteMenu(
            @RequestParam(required = true)
            Long menuId,

            Model model
    ) {
        Menu menuToDelete = managerService.showMenuDetails(menuId);

        model.addAttribute("menuToDelete", menuToDelete);
        model.addAttribute("menuId", menuId);

        return "deleteMenu";
    }

    // 관리자가 삭제를 시도한 기존 상품 데이터를 삭제한다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @PostMapping("/menu/editAndDelete/delete/process")
    public String deleteMenuProcess(
            @RequestParam(required = true)
            Long menuId,

            Model model
    ) {

        // managerService의 'deleteMenu' 메서드로 삭제하고자 하는 데이터의 PK인 ID를 이용하여 데이터를 찾은 다음,
        // 해당 데이터를 DB에서 삭제시킨다.
        managerService.deleteMenu(menuId);

        // 데이터 삭제 이 후, 성공적으로 삭제 했다는 메세지를 출력하는 html 페이지를 보여준다.
        return "deleteSuccess";
    }
}

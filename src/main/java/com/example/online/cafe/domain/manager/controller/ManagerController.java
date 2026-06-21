package com.example.online.cafe.domain.manager.controller;

import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import com.example.online.cafe.domain.manager.service.ManagerService;
import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.dto.MenuManagerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/menu")
    public String showManagerMenu(
            @RequestParam(defaultValue = "0")
            int page
    ) {
        return "managerMenu";
    }

    // 관리자에게 새로운 상품 데이터를 생성 및 DB 테이블에 삽입할 수 있게 해준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/add")
    public String addMenu(

    ) {

        return "addMenu";
    }



    // 밑에 있는 함수들은 관리자가 기존 상품 데이터들을 수정 및 삭제 할 수 있게 해주는 함수들이다.
    // 관리자에게 보여줄 상품 목록 데이터를 'ManagerService' 클래스를 이용해 'MenuManagerDto' 에서 가져와 반환한다.
    // 그럼 다음, 각 기능마다 가지고 있는 전용 html 페이지를 통해서 해당 기능들을 이용할 수 있게 한다.(GET 요청)
    // 전용 html 페이지 다음으로는 각 기능들의 작업을 수행하고 수행 이 후에는 작업이 성공적으로 끝났다는 별도의 html 페이지에서 메세지를 표기한다.(POST 요청)

    // 관리자에게 기존의 상품 데이터를 수정할 수 있게 해준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/edit")
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

        return "editMenu";
    }

    // 관리자가 수정을 시도한 기존 상품 데이터의 수정 내용을 적용한다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @PostMapping("/menu/edit/process")
    public String editMenuProcess() {


        return "editSuccess";
    }

    // 관리자에게 기존의 상품 데이터를 삭제할 수 있게 해준다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @GetMapping("/menu/delete")
    public String deleteMenu(

    ) {


        return "deleteMenu";
    }

    // 관리자가 삭제를 시도한 기존 상품 데이터를 삭제한다.
    // 이곳은 관리자가 로그인 상태여야 진입이 가능하다.
    @PostMapping("/menu/delete/process")
    public String deleteMenuProcess() {


        return "deleteSuccess";
    }
}

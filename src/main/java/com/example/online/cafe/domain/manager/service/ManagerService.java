package com.example.online.cafe.domain.manager.service;

import com.example.online.cafe.domain.manager.dto.ManagerDto;
import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import com.example.online.cafe.domain.menu.dto.MenuManagerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
관리자(Manager)가 이용할 수 있는 기능들을 모은 곳

* 신규 상품 등록
* 기존 상품 목록 조회(각 상품 별 판매량, 매출, 별점, 리뷰 정보 포함)
* 기존 상품 수정
* 기존 상품 삭제

*/

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final MenuRepository menuRepository;

    // 관리자에게 상품 목록에서 보여줄 요소들을 가져온다.
    public MenuManagerDto<Menu> showManagerMenu(int page, Integer price) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        50,
                        Sort.by("id").ascending()
                );

        Page<Menu> managerMenu;

        if (price != null) {
            //customerMenu = menuRepository.showMenuByPriceLowerThan(price, pageable);
            managerMenu = menuRepository.findByPriceLessThan(price, pageable);
        }

        else {
            managerMenu = menuRepository.findAll(pageable);
        }

        // Jpa Repository의 findAll() 특성상 모든 걸 포함하여 보낸다.
        // 다만 Controller에서 보여줄 것들만 분리시켜 보여준다.
        // 나중에 findAll()의 대해 더 자세히 파악하면 여기서부터 보여줄 요소들만 분리시켜 보내게 해야 한다.
        return new MenuManagerDto<>(
                managerMenu.getContent()
        );
    }

    // 관리자에게 특정 상품의 대한 정보들을 가져온다.
    public Menu showMenuDetails(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
    }

    // 관리자에게 새로운 상품 데이터를 생성할 수 있게 해준다.
    public void createMenu() {

    }

    // 관리자에게 기존에 있던 상품의 데이터를 수정할 수 있게 해준다.
    @Transactional
    public void editMenu(Long menuId, String menu_name, Integer price) {
        Menu menuToEdit = showMenuDetails(menuId);

        System.out.println("original name: " + menuToEdit.getMenu_name());
        System.out.println("original price: " + menuToEdit.getPrice());

        menuToEdit.updateMenuName(menu_name);
        menuToEdit.updateMenuPrice(price);

        System.out.println("new name: " + menuToEdit.getMenu_name());
        System.out.println("new price: " + menuToEdit.getPrice());

        // .save() 메서드로도 수정을 DB에 반영할 수 있다.
        // 그러나 @Transactional 어노테이션을 쓴다면 .save() 메서드는 굳이 쓸 필요가 없다.
        // 이 어노테이션은 일반적으로 기존 데이터를 수정할 떄 쓰이며, 새로운 데이터를 생성 및 삽입할 때는 .save()를 써야한다.
        // menuRepository.save(menuToEdit);
    }

    // 관리자에게 기존에 있던 상품의 데이터를 삭제하게 해준다.
    public void deleteMenu(Long menuId) {
        Menu menuToDelete = showMenuDetails(menuId);

        menuRepository.delete(menuToDelete);
    }

}

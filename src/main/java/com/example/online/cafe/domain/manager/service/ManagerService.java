package com.example.online.cafe.domain.manager.service;

import com.example.online.cafe.domain.manager.dto.ManagerDto;
import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import com.example.online.cafe.domain.menu.dto.MenuManagerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/*
관리자(Manager)가 이용할 수 있는 기능들을 모은 곳

* 신규 상품 등록
* 기존 상품 목록 조회(각 상품 별 판매량, 매출, 별점 정보 포함)
* 기존 상품 수정
* 기존 상품 삭제

*/

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final MenuRepository menuRepository;
    private final ReviewService reviewService;

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

    public void addMenu(String menuName, Integer price) {
        // 새로 추가하려는 상품의 이름이 기존 상품들 중에 없다는 것이 확인되면 새로운 상품 데이트를 생성한다.
        Menu newMenu = Menu.builder()
                .menuName(menuName)
                .price(price)
                .rating(0)
                .sales(0L)
                .sales_volume(0L)
                .reviews(new ArrayList<>())
                .build();

        // 새로 생성한 상품 데이터를 'Menu' 테이블에 삽입한다.
        menuRepository.save(newMenu);
    }

    // 관리자에게 기존에 있던 상품의 데이터를 수정할 수 있게 해준다.
    @Transactional
    public void editMenu(Long menuId, String menu_name, Integer price) {
        Menu menuToEdit = showMenuDetails(menuId);

        System.out.println("original name: " + menuToEdit.getMenuName());
        System.out.println("original price: " + menuToEdit.getPrice());

        menuToEdit.updateMenuName(menu_name);
        menuToEdit.updateMenuPrice(price);

        System.out.println("new name: " + menuToEdit.getMenuName());
        System.out.println("new price: " + menuToEdit.getPrice());

        // .save() 메서드로도 수정을 DB에 반영할 수 있다.
        // 그러나 @Transactional 어노테이션을 쓴다면 .save() 메서드는 굳이 쓸 필요가 없다.
        // 이 어노테이션은 일반적으로 기존 데이터를 수정할 떄 쓰이며, 새로운 데이터를 생성 및 삽입할 때는 .save()를 써야한다.
        // menuRepository.save(menuToEdit);
    }

    // 관리자에게 기존에 있던 상품의 데이터를 삭제하게 해준다.
    // 삭제하고자 하는 상품의 데이터에 포함된 리뷰 데이터들도 같이 삭제한다.
    public void deleteMenu(Long menuId) {
        // 먼저 삭제하고자 하는 상품 데이터를 찾는다.
        Menu menuToDelete = showMenuDetails(menuId);

        // 찾은 상품 데이터에 포함된 모든 리뷰 데이터들을 삭제한다.
        reviewService.deleteReview(menuId);

        // 마지막으로 상품 데이터를 완전히 삭제한다.
        menuRepository.delete(menuToDelete);
    }

}

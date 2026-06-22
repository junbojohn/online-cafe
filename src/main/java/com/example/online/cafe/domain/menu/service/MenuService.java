package com.example.online.cafe.domain.menu.service;

import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/*
고객이 상품 목록을 조회하여 특정 상품을 장바구니에 담을 수 있는 등의 기능들을 모은 곳

상품 목록 조회(각 상품 별 이름, 가격, 별점, 리뷰)

*/

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    // 고객에게 상품 목록에서 보여줄 요소들만 포함하여 상품 목록을 조회하고 보여준다.
    @Transactional(readOnly = true)
    public MenuCustomerDto<Menu> showCustomerMenu(int page, Integer price) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        50,
                        Sort.by("id").ascending()
                );

        Page<Menu> customerMenu;

        if (price != null) {
            //customerMenu = menuRepository.showMenuByPriceLowerThan(price, pageable);
            customerMenu = menuRepository.findByPriceLessThan(price, pageable);
        }

        else {
            customerMenu = menuRepository.findAll(pageable);
        }

        // Jpa Repository의 findAll()를 쓰고 있기 때문에 실제로 보내지는 내용물에 모든 게 포함되어 있다.
        // 다만 Controller에서 보여줄 것들과 뺴야 할 것들을 분리시키고 있다.
        // 나중에 findAll()의 대해 더 자세히 파악하면 여기서부터 보여줄 요소들만 분리시켜 보내게 해야 한다.
        return MenuCustomerDto.<Menu>builder()
                .content(customerMenu.getContent())
                .build();


        //menuCustomerDto = menuRepository.findAll();

        //return new MenuCustomerDto<Menu>();
    }


    /*
    @Transactional(readOnly = true)
    public MenuCustomerDto showCustomerMenu(String menu_name, Long price, double rating, List<Review> reviews) {
        return new MenuCustomerDto(menu_name, price, rating, reviews);
    }
    */

    /*
    //참조: offset_pagination 프로젝트
    public MenuCustomerDto showCustomerMenu(String menu_name) {



        if (menu_name != null) {

        }

        else {

        }

        return MenuCustomerDto.builder()

                .build();
    }
    */
}

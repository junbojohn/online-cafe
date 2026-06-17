package com.example.online.cafe.domain.menu.controller;

import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.service.MenuService;
import com.example.online.cafe.domain.review.dto.ReviewDto;
import com.example.online.cafe.domain.review.entity.Review;
import com.example.online.cafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    private final ReviewService reviewService;


    //고객에게 보여줄 상품 목록 데이터를 'MenuService' 클래스를 이용해 'MenuCustomerDto' 에서 가져와 반환한다.
    @GetMapping("/customer")
    public String showCustomerMenu(
            @RequestParam(defaultValue = "0")
            int page,

            //기본적으로 모든 상품이 50,000원을 넘기지 않는다는 하에 가정하여 기본값을 50000으로 한다. 이래야 기본으로 나오는 메뉴에 모든 상품이 나올 수 있다.
            @RequestParam(defaultValue = "50000")
            Integer price,

            Model model
    ) {

        MenuCustomerDto<Menu> menuCustomerDto = menuService.showCustomerMenu(page, price);

        model.addAttribute("menuCustomerDto", menuCustomerDto);
        model.addAttribute("price", price);

        //System.out.println("Connecting to menu page for customers...");

        return "customerMenu";

        /*
        Thymeleaf를 쓴다면 'model'를 이용하여 그 'model'를 보내는 식으로 데이터를 보내어 페이지에 출력시킬 수 있다.
        만약 'model'를 안 쓴다면 ResponseEntity.ok() 함수(function)을 이용하여 데이터를 출력해볼 수 있다.

        google search: how to display data on html with ResponseEntity.ok



        Thymeleaf를 안 쓴다면 아래와 같이 ResponseEntity.ok()를 써서 데이터를 보낼 수 있다.

        클래스 타입을 먼저 MenuCustomerDto<Menu> 로 바꾼다.

        MenuCustomerDto<Menu> menuCustomerDto = menuService.showCustomerMenu(page, price);

        return ResponseEntity.ok(menuCustomerDto);
        */
    }

    @GetMapping("/customer/reviews")
    public String showReview(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(required = true)
            String menuName,

            @RequestParam(required = true)
            //List<Review> menuReviews,
            //MenuCustomerDto<Menu> menuCustomerDto,
            Long menuId,

            Model model
    ) {
        //MenuCustomerDto<Menu> menuCustomerDto = menuService.showCustomerMenu(page, menuId);
        ReviewDto<Review> reviewDto = reviewService.showReviews(page, menuId);
        //ReviewDto<Review> reviewDto = reviewService.showReviews(page, menuReviews);

        System.out.println(reviewDto.content().size());
        System.out.println(reviewDto.content());

        model.addAttribute("menuName", menuName);
        model.addAttribute("reviewDto", reviewDto);
        //model.addAttribute("menuReviews", menuReviews);

        return "customerMenuReviews";
    }

    /*
    google search: ResponseEntity.ok() java

    데이터만 반환한다면 (Body + 200 OK)
    가장 기본적인 형태로 객체(Object)를 매개변수(Parameter)로 넘기면 'Content-Type'이 자동으로 설정되어 JSON으로 직렬화된다.

    그냥 간단하게 ResponseEntity.ok()를 이용하여 ok() 안에 반환할 데이터를 삽입한다.

    예시 코드:
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    */



    /*
    // 'ResponseWithData' can only be used with '/global/common/ResponseWithData.java' implemented
    @GetMapping("/customer")
    public ResponseEntity<ResponseWithData> showCustomerMenu() {

    }
    */
}

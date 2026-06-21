package com.example.online.cafe.domain.menu.controller;

import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.service.MenuService;
import com.example.online.cafe.domain.orders.service.OrdersService;
import com.example.online.cafe.domain.review.dto.ReviewDto;
import com.example.online.cafe.domain.review.entity.Review;
import com.example.online.cafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    private final ReviewService reviewService;

    private final OrdersService ordersService;


    // 고객에게 보여줄 상품 목록 데이터를 'MenuService' 클래스를 이용해 'MenuCustomerDto' 에서 가져와 반환한다.
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

        // 어떤 상품들을 주문했는지 기록한다. 상품 갯수 만큼의 배열(array) 크기를 정하고 각 배열의 인덱스들을 각 상품들의 고유값(ID)을 가리키게 한다. 배열 안에 특정 인덱스의 값이 0 이상이면 그 인덱스에 해당하는 고유값을 가진 상품을 그 만큼 주문하겠다는 의미다.
        // 만약 인덱스의 값이 0이면 해당 인덱스에 해당하는 상품은 주문 하지 않겠다는 의미다.
        Long[] ordered_menu_quantity = new Long[menuService.showCustomerMenu(page, 50000).content().size()];

        model.addAttribute("menuCustomerDto", menuCustomerDto);
        model.addAttribute("price", price);
        model.addAttribute("ordered_menu_quantity", ordered_menu_quantity);

        return "customerMenu";
    }

    /*
    // 기존에 쓰이던 함수로 @GetMapping을 이용해 단순히 상품 데이터만 보여준다.
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
        //OrdersDto<Orders> ordersDto = new OrdersDto<Orders>();

        model.addAttribute("menuCustomerDto", menuCustomerDto);
        model.addAttribute("price", price);
        model.addAttribute("shoppingCart", );

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
        *
    }
    */

    // 고객에게 특정 상품의 대한 리뷰 데이터를 가져와 보여준다.
    @GetMapping("/customer/reviews")
    public String showReview(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(required = true)
            String menuName,

            @RequestParam(required = true)
            Long menuId,

            Model model
    ) {
        ReviewDto<Review> reviewDto = reviewService.showReviews(page, menuId);

        model.addAttribute("menuName", menuName);
        model.addAttribute("reviewDto", reviewDto);

        return "customerMenuReviews";
    }

    // 고객이 주문할 상품(들)이 담겨진 장바구니를 가지고 주문 데이터(들)를 새로 작성하여 DB에 삽입한다.
    @PostMapping("/customer/orders")
    public String showOrderConfirmation(
            //무엇을 주문했는지, 그리고 주문한 상품들마다 주문한 수량을 기록한다.
            @RequestParam
            Long[] ordered_menu_quantity,

            @RequestParam(required = true)
            String email,

            @RequestParam(required = true)
            String post_code,

            @RequestParam(required = true)
            String road_address,

            @RequestParam(required = true)
            String jibun_address,

            @RequestParam(required = true)
            String detail_address,

            @RequestParam
            String extra_address
    ) {
        Boolean cart_empty = true;

        //System.out.println("from Controller, ordered_menu_quantity size: " + ordered_menu_quantity.length + "\n");

        // 'ordered_menu_quantity' 를 확인하여 최소 한 개의 상품을 주문했는지 확인한다.
        for (Long menu : ordered_menu_quantity) {
            if (menu != null && menu > 0) {
                cart_empty = false;
            }
        }

        // 만약 'ordered_menu_quantity' 에 모든 인덱스의 값이 0이면 고객이 장바구니에 아무것도 담지 않았다는 의미다.
        // 이렇게 될 경우, 그냥 상품 목록 페이지로 리다이렉트 시킨다.
        if (cart_empty) {
            return "redirect:/menu/customer";
        }

        // 만약 'ordered_menu_quantity' 에 최소 하나의 인덱스의 값이 0 이상이면 장바구니에 주문할 상품이 최소 한 개 있다는 뜻이며, 해당 인덱스의 값은 그 상품을 그 수량 만큼 주문하겠다는 의미다.
        // 이렇게 될 경우, 'createOrders' 함수에 'ordered_menu_quantity' 를 보내어 주문할 상품의 종류 갯수 만큼 새로운 데이터를 생성하여 'Orders' 테이블에 삽입한다.
        // 삽입이 끝나고 나면 주문이 성공적으로 접수 되었다는 메세지가 담겨진 html 페이지를 보여준다.
        else {
            //use OrdersService class to create and insert new data into 'orders'
            ordersService.createOrders(ordered_menu_quantity, email, post_code, road_address, jibun_address, detail_address, extra_address);

            return "orderSuccess";
        }
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

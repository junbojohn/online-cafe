package com.example.online.cafe.domain.orders.controller;

import com.example.online.cafe.domain.menu.service.MenuService;
import com.example.online.cafe.domain.orders.dto.OrdersFormDto;
import com.example.online.cafe.domain.orders.dto.OrdersDto;
import com.example.online.cafe.domain.orders.service.OrdersService;
import com.example.online.cafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class OrdersController {
    private final OrdersService ordersService;

    // 고객이 주문할 상품(들)이 담겨진 장바구니를 가지고 주문 데이터(들)를 새로 작성하여 DB에 삽입한다.
    @PostMapping("/menu/orders")
    public String showOrderConfirmation(
            // 'shoppingCart' - 무엇을 주문했는지, 그리고 주문한 상품들마다 주문한 수량이 기록돼 있다.
            @ModelAttribute
            OrdersFormDto shoppingCart,

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

        // 'shoppingCart' 에 담겨진 OrdersDto는 각각 특정 상품 데이터를 가리킨다.
        // 이 OrdersDto들을 조회하여 고객이 어떤 상품들을 얼만큼 주문했는지 확인한다.
        // 만약 최소 하나의 OrdersDto의 수량(menu_quantity)이 1이라면 최소 딱 하나의 상품을 한 개 주문한다는 뜻이 된다.
        // 이 경우에는 장바구니가 비어 있지 않으며, 'cart_empty' 를 false로 바꿔 주문 작업을 해야 한다는 뜻이 된다.
        // 만약 모든 OrdersDto의 수량(menu_quantity)이 0이라면 그 어떤 상품도 주문하지 않았다는 뜻이 된다.
        // 간단하게 말해, 장바구니가 비어있다는 뜻이며, 'cart_empty' 는 true로 남아 장바구니가 비어있다는 뜻을 유지해야 한다.
        for (OrdersDto menu : shoppingCart.getOrders()) {
            if (menu.getMenu_quantity() > 0) {
                cart_empty = false;
            }
        }

        /*
        // Currently not being used
        // 'ordered_menu_quantity' 를 확인하여 최소 한 개의 상품을 주문했는지 확인한다.
        for (Long menu : ordered_menu_quantity) {
            if (menu != null && menu > 0) {
                cart_empty = false;
            }
        }
        */

        // 만약 'cart_empty' 가 true라면 장바구니가 비어있다는 뜻이다.
        // 이렇게 될 경우, 그냥 상품 목록 페이지로 리다이렉트 시킨다.
        if (cart_empty) {
            return "redirect:/customer/menu";
        }

        // 장바구니에 최소 한 개의 상품이 한 개 혹은 그 이상의 수량 만큼 주문하겠다는 내용이 확인되면
        // 'createOrders' 함수에 필요한 정보들을 보내어 주문할 상품의 종류 갯수 만큼 새로운 데이터를 생성하여 'Orders' 테이블에 삽입한다.
        // 삽입이 끝나고 나면 주문이 성공적으로 접수 되었다는 메세지가 담겨진 html 페이지를 보여준다.
        else {
            //use OrdersService class to create and insert new data into 'orders'
            //ordersService.createOrders(ordered_menu_quantity, email, post_code, road_address, jibun_address, detail_address, extra_address);
            ordersService.createOrders(shoppingCart, email, post_code, road_address, jibun_address, detail_address, extra_address);

            return "orderSuccess";
        }
    }
}

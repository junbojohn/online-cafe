package com.example.online.cafe.domain.menu.controller;

import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.menu.service.MenuService;
import com.example.online.cafe.domain.orders.dto.OrdersDto;
import com.example.online.cafe.domain.orders.dto.OrdersFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class MenuController {
    private final MenuService menuService;

    private final MenuRepository menuRepository;


    // 고객에게 보여줄 상품 목록 데이터를 'MenuService' 클래스를 이용해 'MenuCustomerDto' 에서 가져와 반환한다.
    @GetMapping("/menu")
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
        // * 현재 가격 필터링을 적용한 채로 주문을 하면 엉뚱한 상품 ID 값이 이용되는 버그가 있다. 배열(array)말고 다른 방법을 사용할 것.
        // 예시: 20개의 상품들 중에서 가격을 1500 이하로 필터링을 적용하면 8개가 나오는데 본래 상품 ID값이 8(Cocoa)인게 필터링을 적용하면 ID값이 5로 나온다
        //Long[] ordered_menu_quantity = new Long[menuService.showCustomerMenu(page, 50000).content().size()];

        // 어떤 상품들을 주문했는지 기록하기 위한 변수인 'shoppingCart' 를 만든다.
        OrdersFormDto shoppingCart = new OrdersFormDto();

        // 'menuCustomerDto' 의 content()를 가져와 안에 있는 모든 'Menu' 데이터를 조회하며 'shoppingCart' 의 구성을 준비한다.
        // 'menuCustomerDto' 의 content()를 조회하는 이유는 바로 가격 필터링 적용 여부에 따라 가져오는 상품 데이터들이 다르기 때문이다.
        // 가격 필터링 적용 여부에 따라 올바른 상품 ID 값으로 올바른 상품 데이터를 찾아내어 주문 데이터에 삽입할 수 있게 된다.
        for (Menu menu : menuCustomerDto.content()) {
            OrdersDto dto = OrdersDto.builder()
                    .menu_id(menu.getId())
                    .menu_quantity(0L)
                    .build();

            shoppingCart.getOrders().add(dto);
        }

        /*
        for (Menu menu : menuList) {
            OrdersDto dto = OrdersDto.builder()
                    .menu_id(menu.getId())
                    .menu_quantity(0L)
                    .build();

            shoppingCart.getOrders().add(dto);
        }
        */

        model.addAttribute("menuCustomerDto", menuCustomerDto);
        model.addAttribute("price", price);
        model.addAttribute("shoppingCart", shoppingCart);

        //model.addAttribute("ordered_menu_quantity", ordered_menu_quantity);

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

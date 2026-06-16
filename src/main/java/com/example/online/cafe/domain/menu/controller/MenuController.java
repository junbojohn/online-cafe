package com.example.online.cafe.domain.menu.controller;

import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;


    //고객에게 보여줄 상품 목록 데이터를 'MenuService' 클래스를 이용해 'MenuCustomerDto' 에서 가져와 반환한다.
    @GetMapping("/customer")
    public ResponseEntity<MenuCustomerDto> showCustomerMenu(
            @RequestParam(defaultValue = "0")
            int page,

            //기본적으로 모든 상품이 50,000원을 넘기지 않는다는 하에 가정하여 기본값을 50000으로 한다. 이래야 기본으로 나오는 메뉴에 모든 상품이 나올 수 있다.
            @RequestParam(defaultValue = "50000")
            Integer price,

            Model model
    ) {
        MenuCustomerDto<Menu> menuCustomerDto = menuService.showCustomerMenu(page, price);

        /*
        Thymeleaf를 쓴다면 'model'를 이용하여 그 'model'를 보내는 식으로 데이터를 보내어 페이지에 출력시킬 수 있다.
        그러나 여기선 실험삼아 'model'이 아닌 ResponseEntity.ok() 함수(function)을 이용하여 데이터를 출력해보자 한다.

        google search: how to display data on html with ResponseEntity.ok
        */

        return ResponseEntity.ok(menuCustomerDto);
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

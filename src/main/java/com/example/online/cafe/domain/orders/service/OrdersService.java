package com.example.online.cafe.domain.orders.service;

import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.orders.dto.OrdersFormDto;
import com.example.online.cafe.domain.orders.entity.Orders;
import com.example.online.cafe.domain.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*
고객이 넣은 주문과 관련된 데이터 작업을 담당하는 곳

* 주문 데이터 생성 및 삽입
* 생성한 주문 데이터의 배송 여부 값을 특정 시간(한국 시간 매일 오후 2시)마다 자동으로 변경

*/

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final MenuRepository menuRepository;

    // 새로운 주문 데이터를 고객이 주문한 내용을 토대로 생성 및 DB 테이블에 삽입한다.
    // 주문 내용에 다른 상품이 여러 개 있으면 그 갯수 만큼 새로운 데이터를 생성하고, 그 새로 만든 모든 데이터들에 하나의 동일한 주문 번호를 부여하여 동일한 주문에 포함된 상품들이라는 것을 표기한다.
    public void createOrders(OrdersFormDto shoppingCart, String email, String post_code, String road_address, String jibun_address, String detail_address, String extra_address) {

        // 'Orders' 테이블에 있는 모든 데이터를 가져온다.
        List<Orders> existingOrdersData = ordersRepository.findAll();

        // 주문 번호를 생성한다.
        Long order_number = ThreadLocalRandom.current().nextLong(0, 9223372036854775806L); //19 digits

        // 새로 생성한 주문 번호를 가진 데이터가 'Orders' 테이블에 이미 존재하는지 확인한다.
        // 만약 존재한다면 주문 번호를 다시 새로 생성한다.
        Boolean alreadyExist = false;

        do {
            alreadyExist = false;

            for (Orders comparingData : existingOrdersData) {
                if (comparingData.getOrder_number() == order_number) {
                    alreadyExist = true;
                    order_number = ThreadLocalRandom.current().nextLong(0, 9223372036854775806L);
                    break;
                }
            }

        } while (alreadyExist);

        //for loop를 이용하여 제공받은 'shoppingCart' 를 조회한다
        for (int i = 0; i < shoppingCart.getOrders().size(); i++) {

            // 만약 특정 인덱스의 'menu_quantity' 의 값이 0 이상이면 해당 인덱스에 해당하는 특정 상품을 'menu_quantity' 의 값 만큼 주문하겠다는 의미다.
            //if (ordered_menu_quantity[i] != null && ordered_menu_quantity[i] > 0) {
            if (shoppingCart.getOrders().get(i).getMenu_quantity() > 0) {

                // 주문 번호도 생성되었으니 이제 builder()를 이용하여 삽입할 정보들을 삽입하고 데이터를 생성한다.
                // order_number: 특정 주문을 가리키는 주문 번호. 주문 데이터 자체는 여러 개 씩 생성되나 이 주문 번호로 어떤 주문 데이터들이 어떤 주문에 포함되어 있는지 구별할 수 있다.
                // menu_number: 한 주문에 포함되어 있는 상품(들 중 하나). 데이터 당 하나의 상품 데이터만 가리킬 수 있다.
                // menu_quantity: 주문한 상품의 주문 수량
                // email: 주문한 고객의 이메일 주소
                // post_code 및 여러가지 address들: 우편번호 및 도로주소 등의 배송 주소 정보들
                // shipped: 배송 상태 여부. 처음 데이터를 생성할 땐 아직 배송되지 않은 상태임으로 초기값은 'false' 로 한다.
                Orders ordersData = Orders.builder()
                        .order_number(order_number)
                        .menu_number(menuRepository.findById(shoppingCart.getOrders().get(i).getMenu_id()).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다.")))
                        .menu_quantity(shoppingCart.getOrders().get(i).getMenu_quantity())
                        .email(email)
                        .post_code(post_code)
                        .road_address(road_address)
                        .jibun_address(jibun_address)
                        .detail_address(detail_address)
                        .extra_address(extra_address)
                        .shipped(false)
                        .build();

                // 생성한 데이터를 DB의 'Orders' 테이블에 삽입한다.
                ordersRepository.save(ordersData);

                /*
                // 디버깅용 콘솔 프린트 코드들
                System.out.println("i: " + i);
                System.out.println("order_number: " + order_number);
                System.out.println("menu_number: " + ordersData.getMenu_number().getId());
                System.out.println("menu_quantity on [i]: " + ordered_menu_quantity[i]);
                System.out.println("email: " + email);
                System.out.println("address: " + address);
                System.out.println("shipped: " + ordersData.getShipped() + "\n");
                */
            }
        }
    }

    // 고객이 넣은 주문 데이터의 배송 여부를 알리는 열(column) 인 'shipped' 의 값을 특정 시간(한국 시간 오후 2시)마다 자동으로 변경한다
    @Transactional
    public void updateShippedOrders() {
        int count = ordersRepository.updateAllShipped();

        System.out.println(count + " order(s) are being shipped");
    }
}

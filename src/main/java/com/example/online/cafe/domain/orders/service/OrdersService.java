package com.example.online.cafe.domain.orders.service;

import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.orders.dto.OrdersDto;
import com.example.online.cafe.domain.orders.entity.Orders;
import com.example.online.cafe.domain.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*
고객이 특정 상품을 주문하면 주문 내용을 토대로 새로운 주문 데이터를 생성하는 곳

주문 내용에 다른 상품이 여러 개 있으면 그 갯수 만큼 새로운 데이터를 생성하고, 그 새로 만든 모든 데이터들에 하나의 동일한 주문 번호를 부여하여 동일한 주문에 포함된 상품들이라는 것을 표기한다.

*/

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final MenuRepository menuRepository;

    // 새로운 주문 데이터를 생성 및 DB 테이블에 삽입한다.
    public void createOrders(Long[] ordered_menu_quantity, String email, String post_code, String road_address, String jibun_address, String detail_address, String extra_address) {

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

        // 존재하는 모든 상품 데이터를 불러온다. 밑에 새 데이터에다 주문한 상품이 무엇인지 찾아서 삽입하기 위해 필요하다.
        List<Menu> menuData = menuRepository.findAll();

        //for loop를 이용하여 제공받은 'ordered_menu_quantity' 를 조회한다
        for (int i = 0; i < ordered_menu_quantity.length; i++) {

            // 만약 특정 인덱스의 값이 0 이상이면 해당 인덱스에 해당하는 고유값(ID)을 가진 상품을 그 만큼 주문하겠다는 의미다.
            if (ordered_menu_quantity[i] != null && ordered_menu_quantity[i] > 0) {

                // 주문할 상품의 수량이 0 이상임을 확인하면, 위에서 불러온 상품 데이터들을 조회하여 해당 상품의 데이터를 가져온다.
                // 'i' 자체의 값이 특정 상품의 고유값(ID)를 가리키니 'i'와 상품 데이터의 고유값(ID)을 비교하는 식으로 올바른 상품 데이터를 찾는다.
                // 다만 'i'의 값은 무조건 0에서 시작하고 상품의 고유값(ID)은 1에서 시작하니 'i' 에다 1를 더해야 상품 데이터를 제대로 찾을 수 있다.
                for (Menu comparingData : menuData) {
                    if (comparingData.getId() == (i + 1)) {

                        // 주문 번호도 생성되었고, 주문할 상품의 데이터도 찾았으니 이제 builder()를 이용하여 삽입할 정보들을 삽입하고 데이터를 생성한다.
                        // order_number: 특정 주문을 가리키는 주문 번호. 주문 데이터 자체는 여러 개 씩 생성되나 이 주문 번호로 어떤 주문 데이터들이 어떤 주문에 포함되어 있는지 구별할 수 있다.
                        // menu_number: 한 주문에 포함되어 있는 상품(들 중 하나). 데이터 당 하나의 상품 데이터만 가리킬 수 있다.
                        // menu_quantity: 주문한 상품의 주문 수량
                        // email: 주문한 고객의 이메일 주소
                        // post_code 및 여러가지 address들: 우편번호 및 도로주소 등의 배송 주소 정보들
                        // shipped: 배송 상태 여부. 처음 데이터를 생성할 땐 아직 배송되지 않은 상태임으로 초기값은 'false' 로 한다.
                        Orders ordersData = Orders.builder()
                                .order_number(order_number)
                                .menu_number(comparingData)
                                .menu_quantity(ordered_menu_quantity[i])
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

                        // 주문 데이터를 생성 및 삽입한 이 후에는 바로 break로 빠져 나와 'ordered_menu_quantity' 배열(array)의 다음 인덱스를 조회한다.
                        break;
                    }
                }
            }
        }
    }

    @Transactional
    public void updateShippedOrders() {
        int count = ordersRepository.updateAllShipped();

        System.out.println(count + " order(s) are being shipped");
    }
}

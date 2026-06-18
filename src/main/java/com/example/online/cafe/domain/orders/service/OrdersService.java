package com.example.online.cafe.domain.orders.service;

import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.orders.dto.OrdersDto;
import com.example.online.cafe.domain.orders.entity.Orders;
import com.example.online.cafe.domain.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void createOrders(Long[] ordered_menu_quantity, String email, String address) {
        System.out.println("Now entered createOrders");

        // 'Orders' 테이블에 있는 모든 데이터를 가져온다.
        List<Orders> existingOrdersData = ordersRepository.findAll();

        // 주문 번호를 생성한다.
        Long order_number = ThreadLocalRandom.current().nextLong(0, 9223372036854775806L); //19 digits

        // 새로 생성한 주문 번호를 가진 데이터가 Orders 테이블에 이미 존재하는지 확인한다.
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

        System.out.println("order_number created: " + order_number);

        // 존재하는 모든 상품 데이터를 불러온다. 밑에 새 데이터에다 주문할 상품이 무엇인지 찾아서 삽입하기 위해 필요하다.
        List<Menu> menuData = menuRepository.findAll();

        //for loop를 이용하여 제공받은 'ordered_menu_quantity' 를 조회한다
        for (int i = 0; i < ordered_menu_quantity.length; i++) {

            System.out.println("Now looping through ordered_menu_quantity array");

            // 만약 특정 인덱스의 값이 0 이상이면 해당 인덱스에 해당하는 고유값(ID)을 가진 상품을 그 만큼 주문하겠다는 의미다.
            if (ordered_menu_quantity[i] != null && ordered_menu_quantity[i] > 0) {

                // 주문할 상품의 수량이 0 이상임을 확인하면, 새로운 데이터를 생성한다.
                //Orders ordersData = new Orders();

                // 새 데이터의 주문 번호를 설정한다.
                //ordersData.setOrder_number(order_number);

                // 새 데이터의 주문할 상품이 무엇인지 설정한다.
                // 인덱스 'i'의 값 자체가 특정 상품의 고유값(ID)를 가리키니 'i'와 상품 데이터의 고유값(ID)을 비교하는 식으로
                // 올바른 상품 데이터를 찾아서 그걸 새 주문 데이터의 'menu_number' 에 넣는다.
                for (Menu comparingData : menuData) {
                    if (comparingData.getId() == (i + 1)) {
                        //ordersData.setMenu_number(comparingData);

                        Orders ordersData = Orders.builder()
                                .order_number(order_number)
                                .menu_number(comparingData)
                                .menu_quantity(ordered_menu_quantity[i])
                                .email(email)
                                .address(address)
                                .shipped(false)
                                .build();

                        ordersRepository.save(ordersData);


                        System.out.println("i: " + i);
                        System.out.println("order_number: " + order_number);
                        System.out.println("menu_number: " + ordersData.getMenu_number().getId());
                        System.out.println("menu_quantity on [i]: " + ordered_menu_quantity[i]);
                        System.out.println("email: " + email);
                        System.out.println("address: " + address);
                        System.out.println("shipped: " + ordersData.getShipped() + "\n");

                        break;
                    }
                }

                // 주문할 상품의 수량을 설정한다.
                //ordersData.setMenu_quantity(ordered_menu_quantity[i]);

                // 주문한 고객이 입력한 이메일과 배송 주소를 설정한다.
                //ordersData.setEmail(email);
                //ordersData.setAddress(address);

                // 해당 주문의 배송 여부를 설정한다. 새로운 주문 데이터를 바로 삽입하는 상태임으로 초기값을 'false'로 하여 아직 배송되지 않았다는 걸 의미하게 한다.
                //ordersData.setShipped(false);

                /*
                System.out.println("i: " + i);
                System.out.println("order_number: " + order_number);
                System.out.println("menu_number: " + ordersData.getMenu_number().getId());
                System.out.println("menu_quantity: " + ordered_menu_quantity[i]);
                System.out.println("email: " + email);
                System.out.println("address: " + address);
                System.out.println("shipped: " + ordersData.getShipped() + "\n");

                ordersRepository.save(ordersData);
                */
            }
        }

    }
}

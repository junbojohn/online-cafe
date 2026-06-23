package com.example.online.cafe.domain.orders.dto;

import com.example.online.cafe.domain.menu.entity.Menu;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDto {

    private Long menu_id;

    private Long menu_quantity;

    /*
    // Currently not being used
    private Long order_number;

    private Menu menu_number;

    private Long menu_quantity;

    private String email;

    private String post_code;

    private String road_address;

    private String jibun_address;

    private String detail_address;

    private String extra_address;

    private Boolean shipped;
    */
}

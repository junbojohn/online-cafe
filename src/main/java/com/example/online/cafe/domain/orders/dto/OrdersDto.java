package com.example.online.cafe.domain.orders.dto;

import com.example.online.cafe.domain.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class OrdersDto {
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
}

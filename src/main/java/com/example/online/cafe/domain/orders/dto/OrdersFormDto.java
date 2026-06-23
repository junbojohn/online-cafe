package com.example.online.cafe.domain.orders.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrdersFormDto {
    private List<OrdersDto> orders = new ArrayList<>();
}

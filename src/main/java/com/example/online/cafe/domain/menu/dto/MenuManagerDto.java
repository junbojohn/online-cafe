package com.example.online.cafe.domain.menu.dto;

import lombok.Builder;

/*
관리자가 상품들을 관리할 수 있도록 보여줄 수 있는 전용 상품 목록 페이지에서 보여줄 것들만 가져와 보여준다.

메뉴 이름(menu_name), 가격(price), 별점(rating), 매출(sales), 판매량(sales_volume)

*/

@Builder
public record MenuManagerDto(String menu_name, Long price, double rating, Long sales, Long sales_volume) {

}

package com.example.online.cafe.domain.menu.dto;

import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/*
고객에게 보여줄 상품 목록에서 보여줄 필요가 없거나 보여줘선 안될 것들 빼고 가져올 데이터들만 가져온다.

메뉴 이름(menu_name), 가격(price), 별점(rating), 리뷰(reviews)

*/

// 본 파일을 class에서 record로 변경해볼 것.
// 참조: https://m.blog.naver.com/seek316/223341255150

//@Getter
@Builder
//@Data
//@AllArgsConstructor
//public record MenuCustomerDto(String menu_name, Long price, double rating, List<Review> reviews)
public record MenuCustomerDto<T>(List<T> content) {
    /*
    //Not being used if this is a record

    public String menu_name;

    public Long price;

    public int rating;

    public List<Review> reviews = new ArrayList<>()
    */


    /*
    //Not being used but kept just in case
    //Code reference from: https://github.com/KangHayeonn/together-party-tonight-server/blob/main/togetherPartyTonight/src/main/java/webProject/togetherPartyTonight/domain/member/dto/response/MemberInfoResponseDto.java

    public MenuCustomerDto showCustomerMenu(Menu menu) {
        return new MenuCustomerDto(menu.getMenu_name());
    }
    */
}

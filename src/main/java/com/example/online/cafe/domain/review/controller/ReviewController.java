package com.example.online.cafe.domain.review.controller;

import com.example.online.cafe.domain.menu.dto.MenuCustomerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.service.MenuService;
import com.example.online.cafe.domain.orders.service.OrdersService;
import com.example.online.cafe.domain.review.dto.ReviewDto;
import com.example.online.cafe.domain.review.entity.Review;
import com.example.online.cafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class ReviewController {
    private final MenuService menuService;

    private final ReviewService reviewService;

    // 고객에게 특정 상품의 대한 리뷰 데이터를 가져와 보여준다.
    @GetMapping("/menu/reviews")
    public String showReview(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(required = true)
            String menuName,

            @RequestParam(required = true)
            Long menuId,

            Model model
    ) {
        ReviewDto<Review> reviewDto = reviewService.showReviews(page, menuId);

        model.addAttribute("menuName", menuName);
        model.addAttribute("reviewDto", reviewDto);

        return "customerMenuReviews";
    }

    // 고객이 리뷰를 남길 수 있도록 상품 목록을 먼저 보여준다.
    @GetMapping("/writeReview")
    public String askMenuToReview(
            @RequestParam(defaultValue = "0")
            int page,

            //기본적으로 모든 상품이 50,000원을 넘기지 않는다는 하에 가정하여 기본값을 50000으로 한다. 이래야 기본으로 나오는 메뉴에 모든 상품이 나올 수 있다.
            @RequestParam(defaultValue = "50000")
            Integer price,

            Model model
    ) {

        MenuCustomerDto<Menu> menuCustomerDto = menuService.showCustomerMenu(page, price);

        model.addAttribute("menuCustomerDto", menuCustomerDto);
        model.addAttribute("price", price);

        return "askMenuToReview";
    }

    // 고객이 특정 상품의 대한 리뷰를 남길 수 있게 해준다.
    @GetMapping("/writeReview/writing")
    public String askReviewDetails(
            @RequestParam(required = true)
            String menuName,

            @RequestParam(required = true)
            Long menuId,

            Model model
    ) {

        model.addAttribute("menuName", menuName);
        model.addAttribute("menuId", menuId);

        return "writeReview";
    }

    @PostMapping("/writeReview/writing/reviewSubmit")
    public String reviewSubmit(
            @RequestParam(required = true)
            Long menuId,

            @RequestParam
            String review_details
    ) {

        reviewService.submitReview(menuId, review_details);

        return "reviewSubmitted";
    }
}

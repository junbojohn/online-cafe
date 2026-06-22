package com.example.online.cafe.domain.review.service;

import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import com.example.online.cafe.domain.review.dto.ReviewDto;
import com.example.online.cafe.domain.review.entity.Review;
import com.example.online.cafe.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/*
고객이 특정 상품의 대한 리뷰를 작성하거나 혹은 다른 고객들이 작성한 리뷰들을 열람할 수 있는 기능을 담은 곳

특정 상품의 대한 리뷰 작성 및 리뷰들 열람

*/

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;

    //고객에게 특정 상품의 대한 리뷰만 포함하여 조회하고 보여준다.
    @Transactional(readOnly = true)
    public ReviewDto<Review> showReviews(int page, Long menuId) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        50,
                        Sort.by("id").ascending()
                );

        Page<Review> reviews;

        //reviews = reviewRepository.findAll(pageable);
        //reviews = reviewRepository.findReviewByMenuId(menuId, pageable);
        reviews = reviewRepository.findByMenu_Id(menuId, pageable);

        return ReviewDto.<Review>builder()
                .content(reviews.getContent())
                .build();
    }

    // 새로운 리뷰 데이터를 생성 및 삽입한다.
    // 리뷰 하고자 하는 상품의 데이터를 먼저 찾은 다음, 그 찾은 상품 데이터를 새로운 리뷰 데이터의 일부로 삽입한다.
    @Transactional
    public void submitReview(Long menuId, String review_details) {
        // 리뷰하고자 하는 상품의 데이터를 찾는다.
        Menu menuToGetReview = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        // 새로운 리뷰 데이터를 생성한다.
        Review newReview = Review.builder()
                .review_details(review_details)
                .menu(menuToGetReview)
                .build();

        // 생성한 리뷰 데이터를 DB에 삽입한다.
        reviewRepository.save(newReview);

        // 생성한 리뷰 데이터를 위에서 찾은 상품 데이터에도 삽입한다.
        menuToGetReview.addReview(newReview);
    }

    // 기존에 있던 상품 데이터를 삭제할 경우, 그 상품 데이터에 있던 리뷰 데이터들도 전부 삭제한다.
    public void deleteReview(Long menuId) {
        // 삭제하려는 상품 데이터를 찾는다.
        Menu menuToDelete = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        // 찾은 상품 데이터에 포함된 모든 리뷰 데이터들을 삭제한다.
        //List<Review> reviewsForSpecificMenu = reviewRepository.findByMenu_Id(menuId);
        List<Review> reviewsForSpecificMenu = reviewRepository.findAll();

        for (Review DataToDelete : reviewsForSpecificMenu) {
            if (DataToDelete.getMenu() == menuToDelete) {
                //System.out.println("Menu to delete: " + menuToDelete.getMenu_name());
                //System.out.println("Review ID to delete: " + DataToDelete.getId());
                //System.out.println("Review details: " + DataToDelete.getReview_details());
                //System.out.println("Menu that this review belongs to: " + DataToDelete.getMenu().getMenu_name() + "\n");

                reviewRepository.delete(DataToDelete);
            }
        }
    }
}

package com.example.online.cafe.domain.review.service;

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

/*
고객이 특정 상품의 대한 리뷰를 작성하거나 혹은 다른 고객들이 작성한 리뷰들을 열람할 수 있는 기능을 담은 곳

특정 상품의 대한 리뷰 작성 및 리뷰들 열람

*/

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

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
}

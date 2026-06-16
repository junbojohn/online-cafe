package com.example.online.cafe.domain.review.service;

import com.example.online.cafe.domain.review.repository.ReviewRepository;
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


}

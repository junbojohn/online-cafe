package com.example.online.cafe.domain.review.controller;

import com.example.online.cafe.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;


}

package com.example.online.cafe.domain.review.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ReviewDto<Review>(List<Review> content) {
}

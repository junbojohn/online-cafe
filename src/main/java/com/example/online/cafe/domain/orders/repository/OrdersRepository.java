package com.example.online.cafe.domain.orders.repository;

import com.example.online.cafe.domain.orders.entity.Orders;
import com.example.online.cafe.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
}

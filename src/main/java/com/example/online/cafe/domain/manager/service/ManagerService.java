package com.example.online.cafe.domain.manager.service;

import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
관리자(Manager)가 이용할 수 있는 기능들을 모은 곳

* 신규 상품 등록
* 기존 상품 목록 조회(각 상품 별 판매량, 매출, 별점, 리뷰 정보 포함)
* 기존 상품 수정
* 기존 상품 삭제

*/

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;


}

package com.example.online.cafe.domain.orders.scheduler;

import com.example.online.cafe.domain.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 지정된 시간 내에 DB의 'Orders' 에 있는 데이터들(를) 수정하게 한다.
// 본 클래스는 한국시간 기준 매일 오후 2시 마다 주문 데이터들의 배송 여부를 지정하는 열(column)인 'shipped' 의 값을 true로 바꾼다.

@Component
@RequiredArgsConstructor
public class OrdersScheduler {
    private final OrdersService ordersService;

    /*
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")

    @Scheduled(
            cron = "0 0 14 * * *",
            zone = "Asia/Seoul"
    )  <- 한국시간(KST) 기준 매일 14:00(오후 2시) 마다 해당 메서드를 실행


    cron 의미:
    초 분 시 일 월 요일
    0 0 14 * * *

    매일 14시(오후 2시)에 실행

    zone 의미:
    'cron' 이 지정한 시간이 어느 시간대(timezone)을 기준으로 할 지 정함
    */

    @Scheduled(
            cron = "0 0 14 * * *",
            zone = "Asia/Seoul"
    )
    public void shippedUpdate() {
        ordersService.updateShippedOrders();
    }
}

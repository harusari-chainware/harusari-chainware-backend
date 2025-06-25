package com.harusari.chainware.order.query.dto.request;

import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderSearchRequest(
        String franchiseName,               // 가맹점명 검색
        FranchiseStatus contractStatus,     // 계약 상태 필터
        OrderStatus orderStatus,            // 주문 상태 필터
        LocalDate startDate,                // 주문 등록일 시작
        LocalDate endDate                   // 주문 등록일 종료
) {
}

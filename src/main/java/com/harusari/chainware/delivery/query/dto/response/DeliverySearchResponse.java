package com.harusari.chainware.delivery.query.dto.response;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliverySearchResponse {

    private String trackingNumber;        // 운송장 번호
//    private String warehouseName;         // 창고명
    private String franchiseName;         // 가맹점명
    private String carrier;               // 택배사
    private String orderCode;             // 배송되는 주문 코드
    private DeliveryStatus deliveryStatus;// 배송 상태
    private LocalDateTime startedAt;      // 배송 요청 일시
    private LocalDateTime deliveredAt;    // 배송 완료 일시
}

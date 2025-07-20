package com.harusari.chainware.delivery.query.dto.response;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryTakeBackInfo {
    private String takeBackCode;
    private TakeBackStatus takeBackStatus;
    private int productCount;      // 반품 제품 종류 수
    private int totalQuantity;     // 총 반품 수량
    private int totalPrice;        // 총 반품 금액
}

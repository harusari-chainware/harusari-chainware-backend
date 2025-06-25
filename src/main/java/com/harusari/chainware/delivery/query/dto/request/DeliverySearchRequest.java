package com.harusari.chainware.delivery.query.dto.request;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySearchRequest {
    private String franchiseName;                 // 가맹점명 검색
    private LocalDate startDate;                  // 배송 시작일 필터 (시작)
    private LocalDate endDate;                    // 배송 시작일 필터 (끝)
    private DeliveryStatus deliveryStatus;        // 배송 상태 필터
//    private String warehouseName;                 // 창고명 검색
//    private String warehouseAddress;              // 창고주소 검색
//    private WarehouseStatus warehouseStatus;      // 창고 상태 필터 (예: ACTIVE)
}
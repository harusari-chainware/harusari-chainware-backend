package com.harusari.chainware.delivery.query.dto.request;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySearchRequest {
    private String franchiseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private DeliveryStatus deliveryStatus;
    private String warehouseName;
    private String warehouseAddress;
    private Boolean warehouseStatus;
}
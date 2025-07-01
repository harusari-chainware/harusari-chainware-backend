package com.harusari.chainware.purchase.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseInboundItem {
    private Long purchaseOrderDetailId;
    private LocalDate expirationDate;
}
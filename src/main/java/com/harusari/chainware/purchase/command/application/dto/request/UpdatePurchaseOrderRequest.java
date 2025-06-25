package com.harusari.chainware.purchase.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePurchaseOrderRequest {

    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private List<PurchaseOrderItemRequest> items;
}

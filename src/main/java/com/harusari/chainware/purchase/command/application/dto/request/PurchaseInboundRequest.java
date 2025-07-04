package com.harusari.chainware.purchase.command.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PurchaseInboundRequest {
    private List<PurchaseInboundItem> products;
}
package com.harusari.chainware.purchase.command.application.service;


public interface PurchaseOrderCommandService {

    Long createFromRequisition(Long requisitionId, Long memberId);

    void approve(Long purchaseOrderId, Long approverId);
}
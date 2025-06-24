package com.harusari.chainware.purchase.command.application.service;


import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.purchase.command.application.dto.request.CancelPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.RejectPurchaseOrderRequest;

public interface PurchaseOrderCommandService {

    Long createFromRequisition(Long requisitionId, Long memberId);

    void approve(Long purchaseOrderId, Long approverId);

    void rejectPurchaseOrder(Long memberId, Long purchaseOrderId, RejectPurchaseOrderRequest request);

    void cancelPurchaseOrder(Long memberId, Long purchaseOrderId, CancelPurchaseOrderRequest request, MemberAuthorityType authorityType);

}
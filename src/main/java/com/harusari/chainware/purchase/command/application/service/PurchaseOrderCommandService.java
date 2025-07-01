package com.harusari.chainware.purchase.command.application.service;


import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.purchase.command.application.dto.request.CancelPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.PurchaseInboundRequest;
import com.harusari.chainware.purchase.command.application.dto.request.RejectPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.UpdatePurchaseOrderRequest;

public interface PurchaseOrderCommandService {

    Long createFromRequisition(Long requisitionId, Long memberId);

    void approvePurchaseOrder(Long purchaseOrderId, Long approverId);

    void rejectPurchaseOrder(Long memberId, Long purchaseOrderId, RejectPurchaseOrderRequest request);

    void cancelPurchaseOrder(Long memberId, Long purchaseOrderId, CancelPurchaseOrderRequest request, MemberAuthorityType authorityType);

    void updatePurchaseOrder(Long purchaseOrderId, UpdatePurchaseOrderRequest request, Long memberId);

    void shippedPurchaseOrder(Long purchaseOrderId, Long memberId);

    void inboundPurchaseOrder(Long purchaseOrderId, Long memberId, PurchaseInboundRequest request);
}
package com.harusari.chainware.purchase.command.application.service;

import com.harusari.chainware.exception.purchase.PurchaseOrderErrorCode;
import com.harusari.chainware.exception.purchase.PurchaseOrderException;
import com.harusari.chainware.exception.requisition.RequisitionErrorCode;
import com.harusari.chainware.exception.requisition.RequisitionException;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.purchase.command.application.dto.request.CancelPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.RejectPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.UpdatePurchaseOrderRequest;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrder;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderDetail;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderStatus;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderDetailRepository;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderRepository;
import com.harusari.chainware.purchase.command.infrastructure.PurchaseOrderCodeGenerator;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionItemResponse;
import com.harusari.chainware.requisition.query.mapper.RequisitionQueryMapper;
import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import com.harusari.chainware.vendor.query.service.VendorQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderCommandServiceImpl implements PurchaseOrderCommandService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final PurchaseOrderCodeGenerator codeGenerator;
    private final RequisitionQueryMapper requisitionQueryMapper;
    private final VendorQueryService vendorQueryService;

    @Override
    @Transactional
    public Long createFromRequisition(Long requisitionId, Long memberId) {
        // 1. 품의서 전체 정보 조회 (승인자, 거래처 등)
        RequisitionDetailResponse requisition = requisitionQueryMapper.findRequisitionById(requisitionId, memberId);
        if (requisition == null) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND);
        }

        // 2. 품의서 품목 리스트 조회
        List<RequisitionItemResponse> items = requisitionQueryMapper.findItemsByRequisitionId(requisitionId);
        if (items.isEmpty()) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_ITEM_NOT_FOUND);
        }

        // 3. 총 금액 계산
        long totalAmount = items.stream()
                .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        VendorDetailDto vendorDetail = vendorQueryService.getVendorDetail(requisition.getVendorId()).getVendor();
        Long vendorMemberId = vendorDetail.getMemberId();


        // 4. 발주 생성
        String code = codeGenerator.generate();

        PurchaseOrder order = PurchaseOrder.builder()
                .requisitionId(requisitionId)
                .vendorId(requisition.getVendorId())
                .createdMemberId(requisition.getCreatedMemberId())
                .vendorMemberId(vendorMemberId)
                .purchaseOrderCode(code)
                .totalAmount(totalAmount)
                .purchaseOrderStatus(PurchaseOrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        purchaseOrderRepository.save(order);

        // 5. 발주 품목 생성
        List<PurchaseOrderDetail> poDetails = items.stream()
                .map(item -> PurchaseOrderDetail.builder()
                        .purchaseOrderId(order.getPurchaseOrderId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice().intValue())
                        .build())
                .toList();

        purchaseOrderDetailRepository.saveAll(poDetails);

        return order.getPurchaseOrderId();
    }



    @Override
    @Transactional
    public void updatePurchaseOrder(Long purchaseOrderId, UpdatePurchaseOrderRequest request, Long memberId) {
        // 1. 발주 조회
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND));

        // 2. 수정 권한 확인
        if (!order.getCreatedMemberId().equals(memberId)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_UNAUTHORIZED_WRITER);
        }

        // 3. 수정 가능 상태 확인
        if (!order.getPurchaseOrderStatus().equals(PurchaseOrderStatus.REQUESTED)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_UPDATE_INVALID_STATUS);
        }

        // 4. 총 금액 계산
        long totalAmount = request.getItems().stream()
                .mapToLong(item -> (long) item.getUnitPrice() * item.getQuantity())
                .sum();

        // 5. 발주 정보 업데이트
        order.updateTotalAmount(totalAmount);

        // 6. 기존 품목 삭제 후 재등록
        purchaseOrderDetailRepository.deleteByPurchaseOrderId(purchaseOrderId);

        List<PurchaseOrderDetail> newDetails = request.getItems().stream()
                .map(item -> PurchaseOrderDetail.builder()
                        .purchaseOrderId(purchaseOrderId)
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
//                        .totalPrice((long) item.getUnitPrice() * item.getQuantity())
                        .build())
                .toList();

        purchaseOrderDetailRepository.saveAll(newDetails);
    }


    @Override
    @Transactional
    public void approvePurchaseOrder(Long purchaseOrderId, Long memberId) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_UNAUTHORIZED_VENDOR));

        if (!order.getPurchaseOrderStatus().equals(PurchaseOrderStatus.REQUESTED)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_APPROVE_INVALID_STATUS);
        }

        order.approve();
    }


    @Override
    @Transactional
    public void rejectPurchaseOrder(Long memberId, Long purchaseOrderId, RejectPurchaseOrderRequest request) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND));

        if (!order.getVendorMemberId().equals(memberId)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_UNAUTHORIZED_VENDOR);
        }

        if (!order.getPurchaseOrderStatus().equals(PurchaseOrderStatus.REQUESTED)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_REJECT_INVALID_STATUS);
        }

        order.reject(request.getRejectReason());
    }


    @Override
    @Transactional
    public void cancelPurchaseOrder(Long memberId, Long purchaseOrderId, CancelPurchaseOrderRequest request, MemberAuthorityType authorityType) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND));

        if (!(authorityType == MemberAuthorityType.GENERAL_MANAGER || authorityType == MemberAuthorityType.SENIOR_MANAGER)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_UNAUTHORIZED_WRITER);
        }

        if (order.getPurchaseOrderStatus() != PurchaseOrderStatus.REQUESTED) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_CANCEL_INVALID_STATUS);
        }

        order.cancel(request.getCancelReason());
    }


    @Override
    @Transactional
    public void shippedPurchaseOrder(Long purchaseOrderId, Long memberId) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND));

        if (!order.getVendorMemberId().equals(memberId)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_UNAUTHORIZED_VENDOR);
        }

        order.shipped();
    }


}
package com.harusari.chainware.purchase.command.application.service;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.purchase.command.application.dto.request.CancelPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.RejectPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrder;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderDetail;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderStatus;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderDetailRepository;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderRepository;
import com.harusari.chainware.purchase.command.infrastructure.PurchaseOrderCodeGenerator;
import com.harusari.chainware.requisition.command.application.exception.AccessDeniedException;
import com.harusari.chainware.requisition.command.application.exception.InvalidStatusException;
import com.harusari.chainware.requisition.command.application.exception.NotFoundException;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionItemResponse;
import com.harusari.chainware.requisition.query.mapper.RequisitionQueryMapper;
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

    @Override
    @Transactional
    public Long createFromRequisition(Long requisitionId, Long memberId) {
        // 1. 품의서 전체 정보 조회 (승인자, 거래처 등)
        RequisitionDetailResponse requisition = requisitionQueryMapper.findRequisitionById(requisitionId, memberId);
        if (requisition == null) {
            throw new IllegalArgumentException("존재하지 않는 품의서입니다.");
        }

        // 2. 품의서 품목 리스트 조회
        List<RequisitionItemResponse> items = requisitionQueryMapper.findItemsByRequisitionId(requisitionId);
        if (items.isEmpty()) {
            throw new IllegalStateException("품의서에 품목이 존재하지 않습니다.");
        }

        // 3. 총 금액 계산
        long totalAmount = items.stream()
                .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        // 4. 발주 생성
        String code = codeGenerator.generate();

        PurchaseOrder order = PurchaseOrder.builder()
                .requisitionId(requisitionId)
                .vendorId(requisition.getVendorId())
                .createdMemberId(requisition.getCreatedMemberId())
                .vendorMemberId(requisition.getApprovedMemberId()) // 결재자를 거래처 담당자로 사용
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
    public void approve(Long purchaseOrderId, Long memberId) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new NotFoundException("발주를 찾을 수 없습니다."));

        if (!order.getPurchaseOrderStatus().equals(PurchaseOrderStatus.REQUESTED)) {
            throw new InvalidStatusException("요청 상태의 발주만 승인할 수 있습니다.");
        }

        order.approve();
    }


    @Override
    @Transactional
    public void rejectPurchaseOrder(Long memberId, Long purchaseOrderId, RejectPurchaseOrderRequest request) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new NotFoundException("발주를 찾을 수 없습니다."));

        if (!order.getVendorMemberId().equals(memberId)) {
            throw new AccessDeniedException("해당 발주의 거래처 담당자가 아닙니다.");
        }

        if (!order.getPurchaseOrderStatus().equals(PurchaseOrderStatus.REQUESTED)) {
            throw new InvalidStatusException("요청 상태의 발주만 거절할 수 있습니다.");
        }

        order.reject(request.getRejectReason());
    }


    @Override
    @Transactional
    public void cancelPurchaseOrder(Long memberId, Long purchaseOrderId, CancelPurchaseOrderRequest request, MemberAuthorityType authorityType) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new NotFoundException("발주를 찾을 수 없습니다."));

        if (!(authorityType == MemberAuthorityType.GENERAL_MANAGER || authorityType == MemberAuthorityType.SENIOR_MANAGER)) {
            throw new AccessDeniedException("발주를 취소할 권한이 없습니다.");
        }

        if (order.getPurchaseOrderStatus() != PurchaseOrderStatus.REQUESTED) {
            throw new InvalidStatusException("요청 상태의 발주만 취소할 수 있습니다.");
        }

        order.cancel(request.getCancelReason());
    }

}
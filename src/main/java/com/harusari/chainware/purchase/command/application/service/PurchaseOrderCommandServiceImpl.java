package com.harusari.chainware.purchase.command.application.service;

import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrder;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderDetail;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderStatus;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderDetailRepository;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderRepository;
import com.harusari.chainware.purchase.command.infrastructure.PurchaseOrderCodeGenerator;
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
}
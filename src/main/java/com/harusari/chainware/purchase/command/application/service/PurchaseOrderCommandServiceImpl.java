package com.harusari.chainware.purchase.command.application.service;

import com.harusari.chainware.exception.purchase.PurchaseOrderErrorCode;
import com.harusari.chainware.exception.purchase.PurchaseOrderException;
import com.harusari.chainware.exception.requisition.RequisitionErrorCode;
import com.harusari.chainware.exception.requisition.RequisitionException;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.purchase.command.application.dto.request.CancelPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.PurchaseInboundRequest;
import com.harusari.chainware.purchase.command.application.dto.request.PurchaseInboundItem;
import com.harusari.chainware.purchase.command.application.dto.request.RejectPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.UpdatePurchaseOrderRequest;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrder;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderDetail;
import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderStatus;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderDetailRepository;
import com.harusari.chainware.purchase.command.domain.repository.PurchaseOrderRepository;
import com.harusari.chainware.purchase.command.infrastructure.PurchaseOrderCodeGenerator;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionInfo;
import com.harusari.chainware.requisition.query.dto.response.RequisitionItemInfo;
import com.harusari.chainware.requisition.query.dto.response.RequisitionItemResponse;
import com.harusari.chainware.requisition.query.mapper.RequisitionQueryMapper;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.service.VendorQueryService;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInbound;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInboundRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderCommandServiceImpl implements PurchaseOrderCommandService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final PurchaseOrderCodeGenerator codeGenerator;
    private final RequisitionQueryMapper requisitionQueryMapper;
    private final VendorQueryService vendorQueryService;
    private final WarehouseInboundRepository warehouseInboundRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    @Override
    @Transactional
    public Long createFromRequisition(Long requisitionId, Long memberId) {
        // 1. 품의서 전체 정보 조회 (승인자, 거래처 등)
        RequisitionDetailResponse requisition = requisitionQueryMapper.findRequisitionById(requisitionId, memberId);
        if (requisition == null) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND);
        }

        // 2. 품의서 품목 리스트 조회
        List<RequisitionItemInfo> items = requisitionQueryMapper.findItemsByRequisitionId(requisitionId);
        if (items.isEmpty()) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_ITEM_NOT_FOUND);
        }

        // 3. 총 금액 계산
        long totalAmount = items.stream()
                .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        Long vendorId = requisition.getVendor().getVendorId();
        VendorDetailResponse vendorDetailResponse = vendorQueryService.getVendorDetail(vendorId);
        Long vendorMemberId = vendorDetailResponse.memberId();


        // 4. 발주 생성
        String code = codeGenerator.generate();

        PurchaseOrder order = PurchaseOrder.builder()
                .requisitionId(requisitionId)
                .vendorId(vendorId)
                .warehouseId(requisition.getRequisitionInfo().getWarehouseId())
                .createdMemberId(requisition.getCreatedMember().getMemberId())
                .vendorMemberId(vendorMemberId)
                .purchaseOrderCode(code)
                .totalAmount(totalAmount)
                .purchaseOrderStatus(PurchaseOrderStatus.REQUESTED)
                .dueDate(requisition.getRequisitionInfo().getDueDate())
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
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND));

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

    @Override
    @Transactional
    public void inboundPurchaseOrder(Long purchaseOrderId, Long memberId, PurchaseInboundRequest request) {
        // 1. 발주서 조회 및 상태 검증
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND));

        if (!order.getPurchaseOrderStatus().equals(PurchaseOrderStatus.SHIPPED)) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_SHIP_INVALID_STATUS);
        }

        Long warehouseId = order.getWarehouseId();

        // 2. 발주 상세 품목 조회
        List<PurchaseOrderDetail> details = purchaseOrderDetailRepository.findByPurchaseOrderId(purchaseOrderId);
        if (details.isEmpty()) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.NO_PURCHASE_ORDER_DETAILS);
        }

        Set<Long> validDetailIds = details.stream()
                .map(PurchaseOrderDetail::getPurchaseOrderDetailId)
                .collect(Collectors.toSet());

        // 3. 요청으로 받은 유통기한 매핑
        Map<Long, LocalDate> expirationDateMap = new HashMap<>();
        for (PurchaseInboundItem item : request.getProducts()) {
            Long detailId = item.getPurchaseOrderDetailId();
            if (!validDetailIds.contains(detailId)) {
                throw new PurchaseOrderException(PurchaseOrderErrorCode.INVALID_PURCHASE_ORDER_DETAIL_ID);
            }
            expirationDateMap.put(detailId, item.getExpirationDate());
        }


        // 4. 입고 기록 저장 (warehouse_inbound)
        List<WarehouseInbound> inboundList = details.stream().map(detail -> {
            LocalDate expirationDate = expirationDateMap.get(detail.getPurchaseOrderDetailId());
            if (expirationDate == null) {
                throw new PurchaseOrderException(PurchaseOrderErrorCode.EXPIRATION_DATE_REQUIRED);
            }
            return WarehouseInbound.builder()
                    .purchaseOrderId(order.getPurchaseOrderId())
                    .warehouseId(warehouseId)
                    .productId(detail.getProductId())
                    .unitQuantity(detail.getQuantity())
                    .expirationDate(expirationDate)
                    .inboundedAt(LocalDateTime.now())
                    .build();
        }).toList();
        warehouseInboundRepository.saveAll(inboundList);

        // 5. 재고 반영 (warehouse_inventory)
        for (PurchaseOrderDetail detail : details) {
            warehouseInventoryRepository.findByWarehouseIdAndProductId(warehouseId, detail.getProductId())
                    .ifPresentOrElse(
                            inventory -> inventory.updateQuantity(inventory.getQuantity() + detail.getQuantity(), LocalDateTime.now()),
                            () -> {
                                WarehouseInventory newInventory = WarehouseInventory.builder()
                                        .warehouseId(warehouseId)
                                        .productId(detail.getProductId())
                                        .quantity(detail.getQuantity())
                                        .reservedQuantity(0)
                                        .build();
                                warehouseInventoryRepository.save(newInventory);
                            }
                    );
        }

        // 6. 상태 전이 → WAREHOUSED
        order.warehoused(PurchaseOrderStatus.WAREHOUSED);
    }

}
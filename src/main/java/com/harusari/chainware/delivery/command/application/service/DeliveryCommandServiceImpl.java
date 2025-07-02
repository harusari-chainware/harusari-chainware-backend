package com.harusari.chainware.delivery.command.application.service;

import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartItem;
import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.delivery.exception.DeliveryErrorCode;
import com.harusari.chainware.delivery.exception.DeliveryException;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackDetail;
import com.harusari.chainware.takeback.command.domain.repository.TakeBackDetailRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseOutbound;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseOutboundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCommandServiceImpl implements DeliveryCommandService {

    private final DeliveryRepository deliveryRepository;

    private final OrderDetailRepository orderDetailRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseOutboundRepository warehouseOutboundRepository;
    private final TakeBackDetailRepository takeBackDetailRepository;

    @Override
    public DeliveryCommandResponse startDelivery(Long deliveryId, DeliveryStartRequest request) {
        // 1. 배송 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        // 2. 상태 검증
        if (!DeliveryStatus.REQUESTED.equals(delivery.getDeliveryStatus())) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_STATUS_NOT_REQUESTED);
        }

        // 3. 배송 기본 정보 추출
        Long warehouseId = delivery.getWarehouseId();
        Long orderId = delivery.getOrderId();
        Long takeBackId = delivery.getTakeBackId();
        String trackingNumber = generateTrackingNumber();

        // 4. 재고 차감 로직 실행
        for (DeliveryStartItem item : request.getProducts()) {
            // 4-1. 요청 항목에서 제품 정보 추출
            Long productId;
            Integer quantity;
            LocalDate expirationDate = item.getExpirationDate();

            // 4-2. 배송 상세 관련 검증
            if (takeBackId != null && item.getTakeBackDetailId() != null) {
                // 반품 기반 배송
                TakeBackDetail detail = takeBackDetailRepository.findById(item.getTakeBackDetailId())
                        .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.TAKE_BACK_DETAIL_NOT_FOUND));

                productId = detail.getProductId();
                quantity = detail.getQuantity();
            } else if (orderId != null && item.getOrderDetailId() != null) {
                // 주문 기반 배송
                OrderDetail detail = orderDetailRepository.findById(item.getOrderDetailId())
                        .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.ORDER_DETAIL_NOT_FOUND_FOR_DELIVERY));

                productId = detail.getProductId();
                quantity = detail.getQuantity();
            } else {
                throw new DeliveryException(DeliveryErrorCode.INVALID_DELIVERY_DETAIL_ID);
            }

            // 4-3. 창고 재고 조회 및 검증
            WarehouseInventory inventory = warehouseInventoryRepository
                    .findByWarehouseIdAndProductIdForUpdate(warehouseId, productId)
                    .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.PRODUCT_INVENTORY_NOT_FOUND_FOR_DELIVERY));

            if (inventory.getQuantity() < quantity || inventory.getReservedQuantity() < quantity) {
                throw new DeliveryException(DeliveryErrorCode.INSUFFICIENT_INVENTORY_FOR_DELIVERY);
            }

            // 4-4. 재고 차감
            inventory.decreaseQuantity(quantity, LocalDateTime.now());
            inventory.decreaseReservedQuantity(quantity, LocalDateTime.now());

            // 4-5. 창고 출고 등록
            WarehouseOutbound outbound = WarehouseOutbound.builder()
                    .orderId(orderId)
                    .warehouseId(warehouseId)
                    .deliveryId(deliveryId)
                    .productId(productId)
                    .quantity(quantity)
                    .expiraionDate(expirationDate)
                    .build();

            warehouseOutboundRepository.save(outbound);
        }

        // 5. 상태 변경
        delivery.startDelivery(trackingNumber, request.getCarrier(), LocalDateTime.now());

        return DeliveryCommandResponse.builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .build();
    }

    @Override
    public DeliveryCommandResponse completeDelivery(Long deliveryId) {
        // 1. 배송 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        // 2. 배송 상태 검증
        if (!DeliveryStatus.IN_TRANSIT.equals(delivery.getDeliveryStatus())) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_STATUS_NOT_IN_TRANSIT);
        }

        // 3. 상태 변경
        delivery.completeDelivery(LocalDateTime.now());

        return DeliveryCommandResponse.builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .build();
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
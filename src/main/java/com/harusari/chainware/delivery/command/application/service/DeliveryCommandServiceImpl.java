package com.harusari.chainware.delivery.command.application.service;

import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.delivery.exception.DeliveryErrorCode;
import com.harusari.chainware.delivery.exception.DeliveryException;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCommandServiceImpl implements DeliveryCommandService {

    private final DeliveryRepository deliveryRepository;

    private final OrderDetailRepository orderDetailRepository;
    private final JpaWarehouseInventoryRepository jpaWarehouseInventoryRepository;

    @Override
    public DeliveryCommandResponse startDelivery(Long deliveryId, DeliveryStartRequest request) {
        // 1. 배송 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        // 2. 상태 검증
        if (!DeliveryStatus.REQUESTED.equals(delivery.getDeliveryStatus())) {
            throw new DeliveryException(DeliveryErrorCode.DELIVERY_STATUS_NOT_REQUESTED);
        }

        // 3. 주문 상세 정보 조회
        Long orderId = delivery.getOrderId();
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (details.isEmpty()) {
            throw new DeliveryException(DeliveryErrorCode.ORDER_DETAIL_NOT_FOUND_FOR_DELIVERY);
        }

        // 4. 재고 차감 로직 실행
        for (OrderDetail detail : details) {
            WarehouseInventory inventory = jpaWarehouseInventoryRepository.findByProductId(detail.getProductId())
                    .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.PRODUCT_INVENTORY_NOT_FOUND_FOR_DELIVERY));

            // 수량 차감 처리
            int quantity = detail.getQuantity();
            if (inventory.getQuantity() < quantity || inventory.getReservedQuantity() < quantity) {
                throw new DeliveryException(DeliveryErrorCode.INSUFFICIENT_INVENTORY_FOR_DELIVERY);
            }

            inventory.decreaseQuantity(quantity, LocalDateTime.now());
            inventory.decreaseReservedQuantity(quantity, LocalDateTime.now());
        }

        // 5. 상태 변경
        delivery.startDelivery(request.getTrackingNumber(), request.getCarrier(), LocalDateTime.now());

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
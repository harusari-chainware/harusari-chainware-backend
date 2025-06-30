package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryMethod;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.order.command.application.dto.request.*;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderException;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import com.harusari.chainware.product.command.infrastructure.JpaProductRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final DeliveryRepository deliveryRepository;
    private final JpaWarehouseInventoryRepository jpaWarehouseInventoryRepository;
    private final JpaProductRepository jpaProductRepository;
    private final ProductRepository ProductRepository;

    private final RedissonClient redissonClient;

    // 주문 등록
    @Override
    public OrderCommandResponse createOrder(OrderCreateRequest request, Long memberId) {
        // 0. 주문 상세가 비어있는 경우 예외 처리
        if (request.getOrderDetails() == null || request.getOrderDetails().isEmpty()) {
            throw new OrderException(OrderErrorCode.EMPTY_ORDER_DETAIL);
        }

        // 1. 가격 계산
        int productCount = request.getOrderDetails().size();
        int totalQuantity = 0;
        long totalPrice = 0L;

        List<OrderDetail> details = new ArrayList<>();

        for (OrderDetailCreateRequest d : request.getOrderDetails()) {
            // 1-1. 재고 정보 조회
            int quantity = d.getQuantity();

            // 1-2. 수량 유효성 및 주문 가능 재고량 검증
            if (quantity <= 0) {
                throw new OrderException(OrderErrorCode.INVALID_ORDER_QUANTITY);
            }

            // 1-3. 제품 단가에 대한 가격 계산
            Product product = ProductRepository.findById(d.getProductId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
            int unitPrice = product.getBasePrice();
            long itemTotalPrice = (long) unitPrice * quantity;
            totalQuantity += quantity;
            totalPrice += itemTotalPrice;

            // 1-4. 주문 상세 엔티티 생성
            details.add(OrderDetail.builder()
                    .orderId(null) // 일단 비워두고 나중에 채움
                    .productId(d.getProductId())
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .totalPrice(itemTotalPrice)
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        // 2. 주문 엔티티 생성 및 저장
        Order order = Order.builder()
                .franchiseId(request.getFranchiseId())
                .memberId(memberId)
                .orderCode(generateOrderCode())
                .deliveryDueDate(request.getDeliveryDueDate())
                .productCount(productCount)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // 3. 주문 ID 반영해서 상세 저장
        List<OrderDetail> finalDetails = details.stream()
                .map(detail -> OrderDetail.builder()
                        .orderId(savedOrder.getOrderId())
                        .productId(detail.getProductId())
                        .quantity(detail.getQuantity())
                        .unitPrice(detail.getUnitPrice())
                        .totalPrice(detail.getTotalPrice())
                        .createdAt(detail.getCreatedAt())
                        .build())
                .toList();

        orderDetailRepository.saveAll(finalDetails);

        return OrderCommandResponse.builder()
                .orderId(savedOrder.getOrderId())
                .franchiseId(savedOrder.getFranchiseId())
                .orderStatus(savedOrder.getOrderStatus())
                .createdAt(savedOrder.getCreatedAt())
                .build();

    }

    // 주문 수정
    @Override
    public OrderCommandResponse updateOrder(Long orderId, OrderUpdateRequest request, Long memberId) {
        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 및 상태 검증
        validateOrderOwner(order, memberId);
        if (!OrderStatus.REQUESTED.equals(order.getOrderStatus())) {
            throw new OrderException(OrderErrorCode.CANNOT_UPDATE_ORDER_STATUS);
        }

        // 3. 기존 주문 상세 삭제
        orderDetailRepository.deleteAllByOrderId(orderId);

        // 4. 가격 계산용 변수
        int productCount = request.getOrderDetails().size();
        int totalQuantity = 0;
        long totalPrice = 0L;

        List<OrderDetail> details = new ArrayList<>();

        for (OrderDetailUpdateRequest d : request.getOrderDetails()) {
            // 4-1. 재고 확인
            int quantity = d.getQuantity();
            if (quantity <= 0) {
                throw new OrderException(OrderErrorCode.INVALID_ORDER_QUANTITY);
            }

            // 4-2. 제품 단가에 대한 가격 계산
            Product product = ProductRepository.findById(d.getProductId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
            int unitPrice = product.getBasePrice();
            long itemTotalPrice = (long) unitPrice * quantity;
            totalQuantity += quantity;
            totalPrice += itemTotalPrice;

            // 4-3. 주문 상세 엔티티 생성
            details.add(OrderDetail.builder()
                    .orderId(orderId)
                    .productId(d.getProductId())
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .totalPrice(itemTotalPrice)
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        // 5. 주문 본문 내용 갱신
        order.update(
                productCount,
                totalQuantity,
                totalPrice,
                request.getDeliveryDueDate()
        );

        // 6. 저장
        orderDetailRepository.saveAll(details);

        return OrderCommandResponse.builder()
                .orderId(order.getOrderId())
                .franchiseId(order.getFranchiseId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    // 주문 취소
    @Override
    public OrderCommandResponse cancelOrder(Long orderId, Long memberId) {
        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 및 상태 검증
        validateOrderOwner(order, memberId);
        if (!OrderStatus.REQUESTED.equals(order.getOrderStatus())) {
            throw new OrderException(OrderErrorCode.CANNOT_CANCEL_ORDER);
        }

        // 3. 상태 변경
        order.changeStatus(OrderStatus.CANCELLED, null, LocalDateTime.now());

        return OrderCommandResponse.builder()
                .orderId(order.getOrderId())
                .franchiseId(order.getFranchiseId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    // 주문 승인
    @Override
    public OrderCommandResponse approveOrder(Long orderId, Long memberId) {
        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 및 상태 검증
        if (!OrderStatus.REQUESTED.equals(order.getOrderStatus())) {
            throw new OrderException(OrderErrorCode.CANNOT_APPROVE_ORDER);
        }

        // 3. 상태 변경
        order.changeStatus(OrderStatus.APPROVED, null, LocalDateTime.now());

        // 4. 예약 재고 변경
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        for (OrderDetail detail : details) {
            WarehouseInventory inventory = jpaWarehouseInventoryRepository.findByProductIdForUpdate(detail.getProductId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_INVENTORY_NOT_FOUND));
            inventory.increaseReservedQuantity(detail.getQuantity(), LocalDateTime.now());
        }

        // 4. 배송 등록
        Delivery delivery = Delivery.builder()
                .orderId(order.getOrderId())
                .deliveryMethod(DeliveryMethod.HEADQUARTERS)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        deliveryRepository.save(delivery);

        return OrderCommandResponse.builder()
                .orderId(order.getOrderId())
                .franchiseId(order.getFranchiseId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    // 주문 반려
    @Override
    public OrderCommandResponse rejectOrder(Long orderId, OrderRejectRequest request, Long memberId) {
        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 및 상태 검증
        if (!OrderStatus.REQUESTED.equals(order.getOrderStatus())) {
            throw new OrderException(OrderErrorCode.CANNOT_REJECT_ORDER);
        }
        if (request.getRejectReason() == null || request.getRejectReason().isBlank()) {
            throw new OrderException(OrderErrorCode.REJECT_REASON_REQUIRED);
        }

        // 3. 상태 변경
        order.changeStatus(OrderStatus.REJECTED, request.getRejectReason(), LocalDateTime.now());

        return OrderCommandResponse.builder()
                .orderId(order.getOrderId())
                .franchiseId(order.getFranchiseId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    // 주문 코드 생성
    private String generateOrderCode() {
        // 오늘 날짜를 yyMMdd 형식으로 변환
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 오늘 날짜의 기존 주문 수 조회
        long count = orderRepository.countByCreatedAtBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );

        // 코드 중 시퀀스값 계산
        String sequencePart = String.format("%03d", count + 1);

        return "SO-" + datePart + sequencePart;
    }

    // 권한 검증
    private void validateOrderOwner(Order order, Long memberId) {
        if (!order.getMemberId().equals(memberId)) {
            throw new OrderException(OrderErrorCode.UNAUTHORIZED_ORDER_ACCESS);
        }
    }

}
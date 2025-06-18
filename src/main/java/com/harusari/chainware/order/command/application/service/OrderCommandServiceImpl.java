package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderDetailCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderDetailUpdateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderUpdateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderUpdateInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    // 주문 등록
    @Override
    public OrderCommandResponse createOrder(OrderCreateRequest request) {

        // 1. 가격 계산용 변수
        int productCount = request.getOrderDetails().size();
        int totalQuantity = 0;
        long totalPrice = 0L;

        List<OrderDetail> details = new ArrayList<>();

        for (OrderDetailCreateRequest d : request.getOrderDetails()) {
            // TODO: productService 등으로 상품 가격 조회
            // int unitPrice = getProductPrice(d.getProductId());
            int unitPrice = 1500;

            int quantity = d.getQuantity();
            long itemTotalPrice = (long) unitPrice * quantity;

            totalQuantity += quantity;
            totalPrice += itemTotalPrice;

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
                .memberId(request.getMemberId())
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

    // 주문 코드 생성
    private String generateOrderCode() {

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

    // 주문 수정
    @Override
    public OrderCommandResponse updateOrder(Long orderId, OrderUpdateRequest request) {

        // 1. 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderUpdateInvalidException(OrderErrorCode.ORDER_UPDATE_INVALID));

        // 2. 상태 검증 (REQUESTED 상태일 때만 수정 가능)
        if (!OrderStatus.REQUESTED.equals(order.getOrderStatus())) {
            throw new OrderUpdateInvalidException(OrderErrorCode.ORDER_UPDATE_INVALID);
        }

        // 3. 기존 주문 상세 삭제
        orderDetailRepository.deleteAllByOrderId(orderId);

        // 4. 가격 계산용 변수
        int productCount = request.getOrderDetails().size();
        int totalQuantity = 0;
        long totalPrice = 0L;

        List<OrderDetail> details = new ArrayList<>();

        for (OrderDetailUpdateRequest d : request.getOrderDetails()) {
            // TODO: productService 등으로 상품 가격 조회
//            int unitPrice = getProductPrice(d.getProductId()); // 임시 함수
            int unitPrice = 1500;
            int quantity = d.getQuantity();
            long itemTotalPrice = (long) unitPrice * quantity;

            totalQuantity += quantity;
            totalPrice += itemTotalPrice;

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
                LocalDateTime.now()
        );

        // 6. 저장
        orderDetailRepository.saveAll(details);

        // 7. 응답
        return OrderCommandResponse.builder()
                .orderId(order.getOrderId())
                .franchiseId(order.getFranchiseId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

}
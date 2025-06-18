package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderCommandResponse createOrder(OrderCreateRequest request) {

        // 1. 주문 엔티티 생성 및 저장 (상태는 'REQUESTED', 현재 시간 기준)
        Order order = Order.builder()
                .storeId(request.getStoreId())
                .orderStatus("REQUESTED")
                .orderedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 2. 주문 상세 생성 및 저장
        List<OrderDetail> details = request.getOrderDetails().stream()
                .map(d -> OrderDetail.builder()
                        .orderId(savedOrder.getOrderId())
                        .productId(d.getProductId())
                        .quantity(d.getQuantity())
                        .build())
                .toList();

        orderDetailRepository.saveAll(details);

        // 3. 저장된 주문 정보를 응답 DTO로 변환하여 반환
        return OrderCommandResponse.builder()
                .orderId(savedOrder.getOrderId())
                .storeId(savedOrder.getStoreId())
                .orderStatus(savedOrder.getOrderStatus())
                .orderedAt(savedOrder.getOrderedAt())
                .build();
    }

}
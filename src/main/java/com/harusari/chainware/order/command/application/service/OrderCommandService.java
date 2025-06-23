package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderRejectRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderUpdateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;

public interface OrderCommandService {

    OrderCommandResponse createOrder(OrderCreateRequest request, Long memberId);
    OrderCommandResponse updateOrder(Long orderId, OrderUpdateRequest request, Long memberId);
    OrderCommandResponse cancelOrder(Long orderId, Long memberId);
    OrderCommandResponse approveOrder(Long orderId, Long memberId);
    OrderCommandResponse rejectOrder(Long orderId, OrderRejectRequest request, Long memberId);

}
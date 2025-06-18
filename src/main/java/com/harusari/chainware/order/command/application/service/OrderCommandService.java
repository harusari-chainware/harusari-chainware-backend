package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderRejectRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderUpdateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;

public interface OrderCommandService {

    OrderCommandResponse createOrder(OrderCreateRequest request);

    OrderCommandResponse updateOrder(Long orderId, OrderUpdateRequest request);

    OrderCommandResponse cancelOrder(Long orderId);

    OrderCommandResponse approveOrder(Long orderId);

    OrderCommandResponse rejectOrder(Long orderId, OrderRejectRequest request);

}
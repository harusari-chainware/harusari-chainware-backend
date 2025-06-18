package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;

public interface OrderCommandService {

    OrderCommandResponse createOrder(OrderCreateRequest request);

}
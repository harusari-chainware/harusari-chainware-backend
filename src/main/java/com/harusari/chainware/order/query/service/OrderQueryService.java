package com.harusari.chainware.order.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.OrderSearchDetailResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import org.springframework.data.domain.Pageable;

public interface OrderQueryService {
    PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request, Pageable pageable);
    OrderSearchDetailResponse getOrderDetail(Long orderId);
}

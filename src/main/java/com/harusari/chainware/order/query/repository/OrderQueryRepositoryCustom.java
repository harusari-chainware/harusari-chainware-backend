package com.harusari.chainware.order.query.repository;

import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.OrderSearchDetailResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepositoryCustom {
    Page<OrderSearchResponse> searchOrders(OrderSearchRequest request, Pageable pageable);
    OrderSearchDetailResponse findOrderDetailById(Long orderId);
}
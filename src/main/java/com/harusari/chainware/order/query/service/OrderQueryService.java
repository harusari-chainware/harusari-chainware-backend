package com.harusari.chainware.order.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import org.springframework.data.domain.Pageable;

// interface
public interface OrderQueryService {
    PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request, Pageable pageable);
}

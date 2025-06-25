package com.harusari.chainware.delivery.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.DeliverySearchResponse;
import org.springframework.data.domain.Pageable;

public interface DeliveryQueryService {
    PageResponse<DeliverySearchResponse> searchDeliveries(DeliverySearchRequest request, Pageable pageable);
}

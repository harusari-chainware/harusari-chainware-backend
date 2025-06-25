package com.harusari.chainware.delivery.query.repository;

import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.DeliverySearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryQueryRepositoryCustom {
    Page<DeliverySearchResponse> searchDeliveries(DeliverySearchRequest request, Pageable pageable);
}

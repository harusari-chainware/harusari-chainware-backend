package com.harusari.chainware.delivery.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.DeliveryDetailResponse;
import com.harusari.chainware.delivery.query.dto.response.DeliverySearchResponse;
import com.harusari.chainware.delivery.query.repository.DeliveryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryQueryServiceImpl implements DeliveryQueryService {

    private final DeliveryQueryRepository deliveryQueryRepository;

    @Override
    public PageResponse<DeliverySearchResponse> searchDeliveries(DeliverySearchRequest request, Pageable pageable) {
        return PageResponse.from(deliveryQueryRepository.searchDeliveries(request, pageable));
    }

    @Override
    public DeliveryDetailResponse getDeliveryDetail(Long deliveryId) {
        return deliveryQueryRepository.findDeliveryDetailById(deliveryId);
    }

}

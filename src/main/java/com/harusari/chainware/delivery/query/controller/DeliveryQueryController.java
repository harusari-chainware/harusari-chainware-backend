package com.harusari.chainware.delivery.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.DeliveryDetailResponse;
import com.harusari.chainware.delivery.query.dto.response.DeliverySearchResponse;
import com.harusari.chainware.delivery.query.service.DeliveryQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryQueryController {

    private final DeliveryQueryService deliveryQueryService;

    // 배송 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DeliverySearchResponse>>> searchDeliveries(
            @ModelAttribute DeliverySearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<DeliverySearchResponse> response = deliveryQueryService.searchDeliveries(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 배송 상세 조회
    @GetMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryDetailResponse>> getDeliveryDetail(
            @PathVariable Long deliveryId
    ) {
        DeliveryDetailResponse response = deliveryQueryService.getDeliveryDetail(deliveryId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

}

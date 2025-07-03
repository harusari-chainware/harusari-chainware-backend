package com.harusari.chainware.delivery.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.DeliveryDetailResponse;
import com.harusari.chainware.delivery.query.dto.response.DeliverySearchResponse;
import com.harusari.chainware.delivery.query.service.DeliveryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Tag(name = "배송 Query API", description = "배송 목록, 상세 조회 API")
public class DeliveryQueryController {

    private final DeliveryQueryService deliveryQueryService;

    @GetMapping
    @Operation(summary = "배송 목록 조회", description = "배송 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배송 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<PageResponse<DeliverySearchResponse>>> searchDeliveries(
            @ModelAttribute DeliverySearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<DeliverySearchResponse> response = deliveryQueryService.searchDeliveries(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    @GetMapping("/{deliveryId}")
    @Operation(summary = "배송 상세 조회", description = "배송 ID를 기준으로 배송을 상세 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배송 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<DeliveryDetailResponse>> getDeliveryDetail(
            @PathVariable Long deliveryId
    ) {
        DeliveryDetailResponse response = deliveryQueryService.getDeliveryDetail(deliveryId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

}

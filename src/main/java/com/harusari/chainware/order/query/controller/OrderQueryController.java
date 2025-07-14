package com.harusari.chainware.order.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.order.query.dto.response.AvailableWarehouseResponse;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.MyFranchiseResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchDetailResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import com.harusari.chainware.order.query.service.OrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 Query API", description = "주문 목록, 상세 조회 API")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<PageResponse<OrderSearchResponse>>> searchOrders(
            @ModelAttribute OrderSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<OrderSearchResponse> response = orderQueryService.searchOrders(request, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 조회", description = "주문 ID를 기준으로 주문을 상세 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<OrderSearchDetailResponse>> getOrderDetail(
            @PathVariable Long orderId
    ) {
        OrderSearchDetailResponse response = orderQueryService.getOrderDetail(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    @GetMapping("/my-franchise")
    @Operation(summary = "주문 등록을 위한 담당자의 가맹점 정보 조회", description = "현재 로그인된 담당자의 가맹점 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가맹점 정보 조회 성공")
    })
    public ResponseEntity<ApiResponse<MyFranchiseResponse>> getMyFranchiseInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyFranchiseResponse response = orderQueryService.getMyFranchiseInfo(userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

package com.harusari.chainware.product.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.request.ProductStatusFilter;
import com.harusari.chainware.product.query.dto.response.ProductDetailResponse;
import com.harusari.chainware.product.query.dto.response.ProductListResponse;
import com.harusari.chainware.product.query.service.ProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "제품 조회 API", description = "제품 목록 조회 및 상세 조회 API")
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    @Operation(
            summary = "제품 목록 조회",
            description = "여러 필터 조건(이름, 코드, 카테고리, 보관 유형, 원산지, 유통기한, 생성일, 상태 등)과 페이징을 적용하여 제품 목록을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제품 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(
            @Parameter(description = "제품 이름", example = "감자칩") @RequestParam(required = false) String productName,
            @Parameter(description = "제품 코드", example = "PC-001") @RequestParam(required = false) String productCode,
            @Parameter(description = "하위 카테고리 ID", example = "5") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "보관 유형") @RequestParam(required = false) StoreType storeType,
            @Parameter(description = "원산지", example = "대한민국") @RequestParam(required = false) String origin,
            @Parameter(description = "유통기한(일)", example = "180") @RequestParam(required = false) Integer shelfLife,
            @Parameter(description = "상위 카테고리 이름", example = "식품") @RequestParam(required = false) String topCategoryName,
            @Parameter(description = "하위 카테고리 이름", example = "스낵") @RequestParam(required = false) String categoryName,
            @Parameter(description = "생성일(YYYY-MM-DD)", example = "2025-06-01") @RequestParam(required = false) String createdAt,
            @Parameter(description = "제품 상태 필터(ALL, ACTIVE_ONLY, INACTIVE_ONLY)", example = "ACTIVE_ONLY")
            @RequestParam(required = false) ProductStatusFilter productStatusFilter,
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") Integer size
    ) {
        LocalDateTime createdAtDateTime = null;
        if (createdAt != null && !createdAt.isBlank()) {
            LocalDate date = LocalDate.parse(createdAt);
            createdAtDateTime = date.atStartOfDay();
        }

        ProductSearchRequest request = ProductSearchRequest.builder()
                .productName(productName)
                .productCode(productCode)
                .categoryId(categoryId)
                .storeType(storeType)
                .origin(origin)
                .shelfLife(shelfLife)
                .topCategoryName(topCategoryName)
                .categoryName(categoryName)
                .createdAt(createdAtDateTime)
                .productStatusFilter(productStatusFilter != null ? productStatusFilter : ProductStatusFilter.ACTIVE_ONLY)
                .page(page)
                .size(size)
                .build();

        ProductListResponse response = productQueryService.getProducts(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제품 상세 조회", description = "제품 ID로 단일 제품의 상세 정보를 조회하며, 사용자 권한에 따라 추가 정보를 포함합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제품 상세 조회 성공")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(
            @Parameter(description = "조회할 제품의 ID", example = "100") @PathVariable Long productId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        ProductDetailResponse detail = productQueryService.getProductDetailByAuthority(
                productId,
                userDetails.getMemberAuthorityType(),
                page,
                size
        );
        return ResponseEntity.ok(ApiResponse.success(detail));
    }
}
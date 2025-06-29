package com.harusari.chainware.product.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.request.ProductStatusFilter;
import com.harusari.chainware.product.query.dto.response.ProductDetailResponse;
import com.harusari.chainware.product.query.dto.response.ProductListResponse;
import com.harusari.chainware.product.query.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponse>> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) StoreType storeType,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) Integer shelfLife,
            @RequestParam(required = false) String topCategoryName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String createdAt,
            @RequestParam(required = false) ProductStatusFilter productStatusFilter,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
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

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
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

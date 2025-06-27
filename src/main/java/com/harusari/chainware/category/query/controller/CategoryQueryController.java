package com.harusari.chainware.category.query.controller;

import com.harusari.chainware.category.query.dto.request.CategorySearchRequest;
import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.category.query.service.CategoryQueryService;
import com.harusari.chainware.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    /* 1. 전체 카테고리 목록 (카테고리 기준 페이징) */
    @GetMapping
    public ResponseEntity<ApiResponse<TopCategoryListResponse>> searchCategories(
            @RequestParam(required = false) String topCategoryName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        CategorySearchRequest request = CategorySearchRequest.builder()
                .topCategoryName(topCategoryName)
                .categoryName(categoryName)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();

        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.searchCategories(request)
        ));
    }


    /* 2. 특정 상위 카테고리 → 제품 목록 조회 (제품 기준 페이징) */
    @GetMapping("/top/{topCategoryId}")
    public ResponseEntity<ApiResponse<TopCategoryProductPageResponse>> getTopCategoryDetailWithProducts(
            @PathVariable Long topCategoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getTopCategoryWithPagedProducts(topCategoryId, page, size)
        ));
    }

    /* 3. 특정 카테고리 상세 조회 (상위 카테고리 + 제품 포함) */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDetailResponse>> getCategoryDetail(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getCategoryDetailWithProducts(categoryId, page, size)
        ));
    }
}
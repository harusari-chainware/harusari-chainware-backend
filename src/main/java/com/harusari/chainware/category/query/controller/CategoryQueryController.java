package com.harusari.chainware.category.query.controller;

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
    public ResponseEntity<ApiResponse<TopCategoryListResponse>> getAllCategoriesGrouped(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getAllCategoriesGroupedByTopCategory(page, size)
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
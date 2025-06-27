package com.harusari.chainware.category.query.controller;

import com.harusari.chainware.category.query.dto.request.CategoryByTopCategoryRequest;
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

    @GetMapping
    public ResponseEntity<ApiResponse<TopCategoryListResponse>> getAllCategoryListWithPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getTopCategoryListWithPaging(page, size)
        ));
    }

    @GetMapping("/top/{topCategoryId}")
    public ResponseEntity<ApiResponse<CategoryListResponse>> getCategoriesByTopCategory(
            @PathVariable  Long topCategoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        CategoryByTopCategoryRequest request = CategoryByTopCategoryRequest.builder()
                .topCategoryId(topCategoryId)
                .page(page)
                .size(size)
                .build();

        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getCategoriesByTopCategory(request)
        ));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<ProductListWithPagination>> getCategoryProducts(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getCategoryProducts(categoryId, page, size)
        ));
    }
}
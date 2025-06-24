package com.harusari.chainware.category.query.controller;

import com.harusari.chainware.category.query.dto.response.CategoryDetailResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryDetailResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryResponse;
import com.harusari.chainware.category.query.service.CategoryQueryService;
import com.harusari.chainware.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping
    public ResponseEntity<List<TopCategoryResponse>> getAllCategoryList() {
        return ResponseEntity.ok(categoryQueryService.getCategoryListWithProductCount());
    }

    @GetMapping("/top/{topCategoryId}")
    public ResponseEntity<ApiResponse<TopCategoryDetailResponse>> getTopCategoryDetail(
            @PathVariable Long topCategoryId) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getTopCategoryDetail(topCategoryId)
        ));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDetailResponse>> getCategoryDetail(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getCategoryDetail(categoryId)
        ));
    }
}
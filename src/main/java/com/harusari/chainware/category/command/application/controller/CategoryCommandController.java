package com.harusari.chainware.category.command.application.controller;

import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.CategoryCommandService;
import com.harusari.chainware.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryCommandController {

    private final CategoryCommandService categoryCommandService;

    /* 카테고리 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryCommandResponse>> createCategory(
            @RequestBody @Valid CategoryCreateRequest request) {
        CategoryCommandResponse response = categoryCommandService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 카테고리 수정 */
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryCommandResponse>> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryCreateRequest request) {
        CategoryCommandResponse response = categoryCommandService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 카테고리 삭제 */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        categoryCommandService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
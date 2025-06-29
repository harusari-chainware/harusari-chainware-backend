package com.harusari.chainware.category.command.application.controller;

import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.CategoryCommandService;
import com.harusari.chainware.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "카테고리 API", description = "카테고리 생성, 수정, 삭제 API")
public class CategoryCommandController {

    private final CategoryCommandService categoryCommandService;

    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 생성 성공")
    })
    @PostMapping
    ResponseEntity<ApiResponse<CategoryCommandResponse>> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "카테고리 생성 요청")
            @RequestBody @Valid CategoryCreateRequest request) {
        CategoryCommandResponse response = categoryCommandService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "카테고리 수정", description = "기존 카테고리의 이름과 상위 카테고리를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryCommandResponse>> updateCategory(
            @Parameter(name = "categoryId", description = "수정할 카테고리의 ID", example = "1")
            @PathVariable Long categoryId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 카테고리 정보")
            @RequestBody @Valid CategoryCreateRequest request) {
        CategoryCommandResponse response = categoryCommandService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "카테고리 삭제", description = "지정한 ID의 카테고리를 논리 삭제 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 삭제 성공")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(name = "categoryId", description = "삭제할 카테고리의 ID", example = "1")
            @PathVariable Long categoryId) {
        categoryCommandService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
package com.harusari.chainware.category.query.controller;

import com.harusari.chainware.category.query.dto.request.CategorySearchRequest;
import com.harusari.chainware.category.query.dto.response.CategoryDetailWithProductsResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryListResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryProductPageResponse;
import com.harusari.chainware.category.query.service.CategoryQueryService;
import com.harusari.chainware.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리 조회 API", description = "카테고리 조회 관련 API")
public class CategoryQueryController {

    private final CategoryQueryService categoryQueryService;

    @Operation(summary = "전체 카테고리 목록 조회", description = "상위 카테고리 기준으로 전체 카테고리 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 카테고리 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<TopCategoryListResponse>> searchCategories(
            @Parameter(description = "상위 카테고리 이름", example = "식품") @RequestParam(required = false) String topCategoryName,
            @Parameter(description = "하위 카테고리 이름", example = "과자") @RequestParam(required = false) String categoryName,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준", example = "categoryName") @RequestParam(required = false) String sortBy,
            @Parameter(description = "정렬 방향(asc 또는 desc)", example = "asc") @RequestParam(required = false) String sortDir
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

    @Operation(summary = "특정 상위 카테고리 제품 목록 조회", description = "특정 상위 카테고리에 속한 카테고리와 제품들을 제품 기준 페이징으로 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 상위 카테고리 제품 목록 조회 성공")
    })
    @GetMapping("/top/{topCategoryId}")
    public ResponseEntity<ApiResponse<TopCategoryProductPageResponse>> getTopCategoryDetailWithProducts(
            @Parameter(description = "조회할 상위 카테고리의 ID", example = "1") @PathVariable Long topCategoryId,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getTopCategoryWithPagedProducts(topCategoryId, page, size)
        ));
    }

    @Operation(summary = "특정 카테고리 상세 조회", description = "단일 카테고리의 상세 정보와 해당 카테고리에 속한 제품 목록을 페이징하여 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 카테고리 상세 조회 성공")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDetailWithProductsResponse>> getCategoryDetail(
            @Parameter(description = "조회할 카테고리의 ID", example = "5")  @PathVariable Long categoryId,
            @Parameter(description = "페이지 번호", example = "1")  @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                categoryQueryService.getCategoryDetailWithProducts(categoryId, page, size)
        ));
    }
}
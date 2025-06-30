package com.harusari.chainware.category.command.application.controller;

import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.TopCategoryCommandService;
import com.harusari.chainware.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/topcategory")
@RequiredArgsConstructor
@Tag(name = "상위 카테고리 API", description = "상위 카테고리 생성, 수정, 삭제 API")
public class TopCategoryCommandController {

    private final TopCategoryCommandService topCategoryCommandService;

    @Operation(summary = "상위 카테고리 생성", description = "새로운 상위 카테고리를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상위 카테고리 생성 성공")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TopCategoryCommandResponse>> create(
            @RequestBody(description = "상위 카테고리 생성 요청")
            @Valid @org.springframework.web.bind.annotation.RequestBody TopCategoryCreateRequest request
    ) {
        TopCategoryCommandResponse response = topCategoryCommandService.createTopCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "상위 카테고리 수정", description = "기존 상위 카테고리의 이름 및 설정을 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상위 카테고리 수정 성공")
    })
    @PutMapping("/{topCategoryId}")
    public ResponseEntity<ApiResponse<TopCategoryCommandResponse>> update(
            @Parameter(name = "topCategoryId", description = "수정할 상위 카테고리의 ID", example = "1")
            @PathVariable Long topCategoryId,
            @RequestBody(description = "수정할 상위 카테고리 정보")
            @Valid @org.springframework.web.bind.annotation.RequestBody TopCategoryUpdateRequest request
    ) {
        TopCategoryCommandResponse response = topCategoryCommandService.updateTopCategory(topCategoryId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "상위 카테고리 삭제", description = "지정한 ID의 상위 카테고리를 논리 삭제 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상위 카테고리 삭제 성공")
    })
    @DeleteMapping("/{topCategoryId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(name = "topCategoryId", description = "삭제할 상위 카테고리의 ID", example = "1")
            @PathVariable Long topCategoryId
    ) {
        topCategoryCommandService.deleteTopCategory(topCategoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
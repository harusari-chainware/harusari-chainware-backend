package com.harusari.chainware.takeback.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.TakeBackDetailResponse;
import com.harusari.chainware.takeback.query.dto.response.TakeBackSearchResponse;
import com.harusari.chainware.takeback.query.service.TakeBackQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/takeback")
@RequiredArgsConstructor
@Tag(name = "반품 Query API", description = "반품 목록, 상세 조회 API")
public class TakeBackQueryController {

    private final TakeBackQueryService takeBackQueryService;

    @GetMapping
    @Operation(summary = "반품 목록 조회", description = "반품 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<PageResponse<TakeBackSearchResponse>>> getTakeBackList(
            TakeBackSearchRequest request,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        PageResponse<TakeBackSearchResponse> response = takeBackQueryService.getTakeBackList(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{takeBackId}")
    @Operation(summary = "반품 상세 조회", description = "반품 ID를 기준으로 반품을 상세 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<TakeBackDetailResponse>> getTakeBackDetail(
            @PathVariable Long takeBackId
    ) {
        TakeBackDetailResponse response = takeBackQueryService.getTakeBackDetail(takeBackId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

package com.harusari.chainware.takeback.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackRejectRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import com.harusari.chainware.takeback.command.application.service.TakeBackCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/takeback")
@RequiredArgsConstructor
@Tag(name = "반품 Command API", description = "반품 신청, 취소, 승인, 반려 API")
public class TakeBackCommandController {

    private final TakeBackCommandService takeBackCommandService;

    @PostMapping
    @Operation(summary = "반품 신청", description = "가맹점 관리자가 반품을 신청합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 신청됨")
    })
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> createTakeBack(
            @RequestPart TakeBackCreateRequest request,
            @RequestPart List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.createTakeBack(request, imageFiles, userDetails.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PutMapping("/{takeBackId}/cancel")
    @Operation(summary = "반품 취소", description = "가맹점 관리자가 요청 상태의 반품을 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 취소됨")
    })
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> cancelTakeBack(
            @PathVariable Long takeBackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.cancelTakeBack(takeBackId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{takeBackId}/collect")
    @Operation(summary = "반품 수거", description = "창고 관리자가 반품 ID를 기준으로 반품을 수거합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 수거됨")
    })
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> collectTakeBack(
            @PathVariable Long takeBackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.collectTakeBack(takeBackId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{takeBackId}/approve")
    @Operation(summary = "반품 승인", description = "창고 관리자가 반품 ID를 기준으로 반품을 승인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 승인됨")
    })
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> approveTakeBack(
            @PathVariable Long takeBackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.approveTakeBack(takeBackId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{takeBackId}/reject")
    @Operation(summary = "반품 반려", description = "창고 관리자가 반품 ID를 기준으로 반품을 반려합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 반려됨")
    })
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> rejectTakeBack(
            @PathVariable Long takeBackId,
            @RequestBody TakeBackRejectRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.rejectTakeBack(takeBackId, request, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

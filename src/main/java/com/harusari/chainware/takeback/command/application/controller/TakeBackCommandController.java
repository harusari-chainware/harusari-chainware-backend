package com.harusari.chainware.takeback.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackRejectRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import com.harusari.chainware.takeback.command.application.service.TakeBackCommandService;
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
public class TakeBackCommandController {

    private final TakeBackCommandService takeBackCommandService;

    // 반품 신청
    @PostMapping
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> createTakeBack(
            @RequestPart TakeBackCreateRequest request,
            @RequestPart List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.createTakeBack(request, imageFiles, userDetails.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    // 반품 취소
    @PutMapping("/{takeBackId}/cancel")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> cancelTakeBack(
            @PathVariable Long takeBackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.cancelTakeBack(takeBackId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 수거
    @PutMapping("/{takeBackId}/collect")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> collectTakeBack(
            @PathVariable Long takeBackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.collectTakeBack(takeBackId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 승인
    @PutMapping("/{takeBackId}/approve")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> approveTakeBack(
            @PathVariable Long takeBackId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TakeBackCommandResponse response = takeBackCommandService.approveTakeBack(takeBackId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 반려
    @PutMapping("/{takeBackId}/reject")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> rejectTakeBack(
            @PathVariable Long takeBackId,
            @RequestBody TakeBackRejectRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails

    ) {
        TakeBackCommandResponse response = takeBackCommandService.rejectTakeBack(takeBackId, request, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

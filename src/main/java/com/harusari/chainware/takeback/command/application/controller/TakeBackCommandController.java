package com.harusari.chainware.takeback.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackRejectRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import com.harusari.chainware.takeback.command.application.service.TakeBackCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/takeback")
@RequiredArgsConstructor
public class TakeBackCommandController {

    private final TakeBackCommandService takeBackCommandService;

    // 반품 신청
    @PostMapping
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> createTakeBack(
            @RequestBody @Valid TakeBackCreateRequest request
    ) {
        TakeBackCommandResponse response = takeBackCommandService.createTakeBack(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 취소
    @PutMapping("/{takeBackId}/cancel")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> cancelTakeBack(
            @PathVariable Long takeBackId
    ) {
        TakeBackCommandResponse response = takeBackCommandService.cancelTakeBack(takeBackId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 수거
    @PutMapping("/{takeBackId}/collect")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> collectTakeBack(
            @PathVariable Long takeBackId
    ) {
        TakeBackCommandResponse response = takeBackCommandService.collectTakeBack(takeBackId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 승인
    @PutMapping("/{takeBackId}/approve")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> approveTakeBack(
            @PathVariable Long takeBackId
    ) {
        TakeBackCommandResponse response = takeBackCommandService.approveTakeBack(takeBackId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 반품 반려
    @PutMapping("/{takeBackId}/reject")
    public ResponseEntity<ApiResponse<TakeBackCommandResponse>> rejectTakeBack(
            @PathVariable Long takeBackId,
            @RequestBody TakeBackRejectRequest request
    ) {
        TakeBackCommandResponse response = takeBackCommandService.rejectTakeBack(takeBackId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

package com.harusari.chainware.takeback.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import com.harusari.chainware.takeback.command.application.service.TakeBackCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 반품 취소: "/api/v1/takeback/{takebackId}", DELETE

    // 반품 수거: "/api/v1/takeback/{takebackId}/warehoused", PUT

    // 반품 승인: "/api/v1/takeback/{takebackId}/approve", PUT

    // 반품 반려: "/api/v1/takeback/{takebackId}/reject", PUT

    // 반품 폐기: "/api/v1/takeback/{takebackId}/disposal", PUT

}

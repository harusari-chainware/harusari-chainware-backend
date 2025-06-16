package com.harusari.chainware.vendor.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.vendor.command.application.dto.VendorCreateRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorStatusChangeRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequestDto;
import com.harusari.chainware.vendor.command.application.service.VendorCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorCommandController {

    private final VendorCommandService vendorCommandService;

    /*
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createVendor(@RequestBody @Valid VendorCreateRequestDto requestDto) {
        Long vendorId = vendorCommandService.createVendor(requestDto);
        return ResponseEntity.ok(ApiResponse.success(vendorId));
    }
    */

    // 거래처 기본 정보 수정
    @PutMapping("/{vendorId}")
    public ResponseEntity<ApiResponse<Void>> updateVendor(
            @PathVariable Long vendorId,
            @RequestBody @Valid VendorUpdateRequestDto requestDto
    ) {
        vendorCommandService.updateVendor(vendorId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 거래처 거래 상태 수정
    @PutMapping("/{vendorId}/status")
    public ResponseEntity<ApiResponse<Void>> changeVendorStatus(
            @PathVariable Long vendorId,
            @RequestBody @Valid VendorStatusChangeRequestDto dto
    ) {
        vendorCommandService.changeVendorStatus(vendorId, dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // 발주 테이블 완성 후 거래가 없을 경우 삭제 구현 해야 함

}

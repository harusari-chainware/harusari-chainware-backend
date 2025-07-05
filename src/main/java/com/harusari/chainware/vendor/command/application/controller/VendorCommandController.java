package com.harusari.chainware.vendor.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequest;
import com.harusari.chainware.vendor.command.application.service.VendorCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "거래처 Command API", description = "거래처 Command API (수정)")
public class VendorCommandController {

    private final VendorCommandService vendorCommandService;

    @Operation(
            summary = "거래처 정보 수정",
            description = "vendorId에 해당하는 거래처 정보를 수정하고 계약서 파일을 함께 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "거래처 수정 성공")
    })
    @PutMapping("/vendors/{vendorId}")
    public ResponseEntity<ApiResponse<Void>> updateVendor(
            @Parameter(description = "수정할 거래처 ID")
            @PathVariable Long vendorId,

            @Parameter(description = "수정할 거래처 정보")
            @RequestPart VendorUpdateRequest vendorUpdateRequest,

            @Parameter(description = "계약서 파일 (PDF)")
            @RequestPart(required = false) MultipartFile agreementFile
    ) {
        vendorCommandService.updateVendor(vendorId, vendorUpdateRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

}
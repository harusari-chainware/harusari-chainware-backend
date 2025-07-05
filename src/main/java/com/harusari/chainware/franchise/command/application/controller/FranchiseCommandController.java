package com.harusari.chainware.franchise.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.franchise.command.application.dto.request.UpdateFranchiseRequest;
import com.harusari.chainware.franchise.command.application.service.FranchiseCommandService;
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
@Tag(name = "가맹점 Command API", description = "가맹점 Command API (수정, 삭제)")
public class FranchiseCommandController {

    private final FranchiseCommandService franchiseCommandService;

    @Operation(
            summary = "가맹점 정보 수정",
            description = "franchiseId에 해당하는 가맹점의 정보를 수정하고 계약서 파일을 함께 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가맹점 수정 성공")
    })
    @PutMapping("franchises/{franchiseId}")
    public ResponseEntity<ApiResponse<Void>> updateFranchise(
            @Parameter(description = "수정할 가맹점 ID")
            @PathVariable(name = "franchiseId") Long franchiseId,

            @Parameter(description = "수정할 가맹점 정보")
            @RequestPart UpdateFranchiseRequest updateFranchiseRequest,

            @Parameter(description = "계약서 파일 (PDF)")
            @RequestPart(required = false) MultipartFile agreementFile
    ) {
        franchiseCommandService.updateFranchise(franchiseId, updateFranchiseRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

}
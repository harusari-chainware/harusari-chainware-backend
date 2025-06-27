package com.harusari.chainware.franchise.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.franchise.command.application.dto.request.UpdateFranchiseRequest;
import com.harusari.chainware.franchise.command.application.service.FranchiseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FranchiseCommandController {

    private final FranchiseCommandService franchiseCommandService;

    @PutMapping("franchises/{franchiseId}")
    public ResponseEntity<ApiResponse<Void>> updateFranchise(
            @PathVariable(name = "franchiseId") Long franchiseId,
            @RequestPart UpdateFranchiseRequest updateFranchiseRequest,
            @RequestPart MultipartFile agreementFile
    ) {
        franchiseCommandService.updateFranchise(franchiseId, updateFranchiseRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

}
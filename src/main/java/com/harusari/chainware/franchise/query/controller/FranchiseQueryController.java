package com.harusari.chainware.franchise.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.service.FranchiseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FranchiseQueryController {

    private final FranchiseQueryService franchiseQueryService;

    @GetMapping("/franchises")
    public ResponseEntity<ApiResponse<PageResponse<FranchiseSearchResponse>>> searchFranchises(
            @ModelAttribute FranchiseSearchRequest franchiseSearchRequest,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FranchiseSearchResponse> franchises = franchiseQueryService.searchFranchises(franchiseSearchRequest, pageable);
        PageResponse<FranchiseSearchResponse> pageResponse = PageResponse.from(franchises);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

}
package com.harusari.chainware.takeback.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.TakeBackSearchResponse;
import com.harusari.chainware.takeback.query.service.TakeBackQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/takeback")
@RequiredArgsConstructor
public class TakeBackQueryController {

    private final TakeBackQueryService takeBackQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TakeBackSearchResponse>>> getTakeBackList(
            TakeBackSearchRequest request,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        PageResponse<TakeBackSearchResponse> response = takeBackQueryService.getTakeBackList(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

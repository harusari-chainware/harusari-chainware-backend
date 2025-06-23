package com.harusari.chainware.category.command.application.controller;

import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.TopCategoryCommandService;
import com.harusari.chainware.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/topcategory")
@RequiredArgsConstructor
public class TopCategoryCommandController {

    private final TopCategoryCommandService topCategoryCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<TopCategoryCommandResponse>> create(@RequestBody @Valid TopCategoryCreateRequest request) {
        TopCategoryCommandResponse response = topCategoryCommandService.createTopCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{topCategoryId}")
    public ResponseEntity<ApiResponse<TopCategoryCommandResponse>> update(@PathVariable Long topCategoryId, @RequestBody @Valid TopCategoryUpdateRequest request) {
        TopCategoryCommandResponse response = topCategoryCommandService.updateTopCategory(topCategoryId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{topCategoryId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long topCategoryId) {
        topCategoryCommandService.deleteTopCategory(topCategoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
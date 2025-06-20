package com.harusari.chainware.category.command.application.controller;

import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.TopCategoryCommandService;
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
    public ResponseEntity<TopCategoryCommandResponse> create(@RequestBody @Valid TopCategoryCreateRequest request) {
        return ResponseEntity.ok(topCategoryCommandService.createTopCategory(request));
    }

    @PutMapping("/{topCategoryId}")
    public ResponseEntity<TopCategoryCommandResponse> update(@PathVariable Long topCategoryId,                                                             @RequestBody @Valid TopCategoryUpdateRequest request) {
        TopCategoryCommandResponse response = topCategoryCommandService.updateTopCategory(topCategoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{topCategoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long topCategoryId) {
        topCategoryCommandService.deleteTopCategory(topCategoryId);
        return ResponseEntity.noContent().build();
    }
}
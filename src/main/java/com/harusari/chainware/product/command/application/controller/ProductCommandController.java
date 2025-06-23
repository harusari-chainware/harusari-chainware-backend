package com.harusari.chainware.product.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.application.dto.request.ProductUpdateRequest;
import com.harusari.chainware.product.command.application.dto.response.ProductCommandResponse;
import com.harusari.chainware.product.command.application.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    @PostMapping("/product/headquarters")
    public ResponseEntity<ApiResponse<ProductCommandResponse>> createProduct(
            @RequestBody @Validated ProductCreateRequest productCreateRequest
    ) {
        ProductCommandResponse response = productCommandService.createProduct(productCreateRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/product/headquarters/{productId}")
    public ResponseEntity<ApiResponse<ProductCommandResponse>> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Validated ProductUpdateRequest productUpdateRequest
    ) {
        ProductCommandResponse response = productCommandService.updateProduct(productId, productUpdateRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/product/headquarters/{productId}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {

        productCommandService.deleteProduct(productId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
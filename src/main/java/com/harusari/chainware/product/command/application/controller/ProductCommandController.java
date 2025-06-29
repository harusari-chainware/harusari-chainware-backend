package com.harusari.chainware.product.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.application.dto.request.ProductUpdateRequest;
import com.harusari.chainware.product.command.application.dto.response.ProductCommandResponse;
import com.harusari.chainware.product.command.application.service.ProductCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "제품 명령 API", description = "제품 생성, 수정 및 삭제 기능을 제공하는 API")
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    @Operation(summary = "제품 생성", description = "새로운 제품을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "제품 생성 성공")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCommandResponse>> createProduct(
            @RequestBody(description = "생성할 제품 정보", required = true)
            @Validated @org.springframework.web.bind.annotation.RequestBody  ProductCreateRequest productCreateRequest
    ) {
        ProductCommandResponse response = productCommandService.createProduct(productCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "제품 수정", description = "기존 제품 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제품 수정 성공")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductCommandResponse>> updateProduct(
            @Parameter(description = "수정할 제품의 ID", example = "42")
            @PathVariable Long productId,
            @RequestBody(description = "수정할 제품 정보")
            @Validated @org.springframework.web.bind.annotation.RequestBody ProductUpdateRequest productUpdateRequest
    ) {
        ProductCommandResponse response = productCommandService.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제품 삭제", description = "지정한 ID의 제품을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제품 삭제 성공")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "삭제할 제품의 ID", example = "42")
            @PathVariable Long productId
    ) {
        productCommandService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
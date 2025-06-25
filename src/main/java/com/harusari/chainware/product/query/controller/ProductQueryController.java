package com.harusari.chainware.product.query.controller;

import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.request.ProductStatusFilter;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import com.harusari.chainware.product.query.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "ACTIVE_ONLY") ProductStatusFilter productStatusFilter
    ) {
        ProductSearchRequest request = ProductSearchRequest.builder()
                .productName(productName)
                .productCode(productCode)
                .categoryId(categoryId)
                .productStatusFilter(productStatusFilter)
                .build();

        return ResponseEntity.ok(productQueryService.getProducts(request));
    }
}

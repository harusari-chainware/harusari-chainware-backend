package com.harusari.chainware.product.query.service;

import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDto;

import java.util.List;

public interface ProductQueryService {
    List<ProductDto> getProducts(ProductSearchRequest request);
    ProductDto getProductById(Long productId);
}

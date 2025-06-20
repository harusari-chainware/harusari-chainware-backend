package com.harusari.chainware.product.command.application.service;

import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.application.dto.request.ProductUpdateRequest;
import com.harusari.chainware.product.command.application.dto.response.ProductCommandResponse;

public interface ProductCommandService {

    ProductCommandResponse createProduct(ProductCreateRequest request);

    ProductCommandResponse updateProduct(Long productId, ProductUpdateRequest request);

    void deleteProduct(Long productId);

}

package com.harusari.chainware.product.query.service;

import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import com.harusari.chainware.product.query.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProducts(ProductSearchRequest request) {
        return productMapper.findProductsByConditions(request);
    }
}

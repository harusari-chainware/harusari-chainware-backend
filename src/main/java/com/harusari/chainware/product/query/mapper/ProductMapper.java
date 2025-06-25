package com.harusari.chainware.product.query.mapper;

import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    List<ProductDto> findProductsByConditions(ProductSearchRequest request);
    Optional<ProductDto> findProductById(Long productId);
}
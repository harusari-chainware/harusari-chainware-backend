package com.harusari.chainware.product.command.application.mapper;

import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    // productCode, productStatus, isDeleted는 매핑하지 않고 나중에 Service에서 처리
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "productStatus", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Product toEntity(ProductCreateRequest request);
}
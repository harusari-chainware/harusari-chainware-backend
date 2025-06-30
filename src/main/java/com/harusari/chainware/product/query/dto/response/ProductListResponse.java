package com.harusari.chainware.product.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductListResponse {
    private List<ProductDto> products;
    private Pagination pagination;
}
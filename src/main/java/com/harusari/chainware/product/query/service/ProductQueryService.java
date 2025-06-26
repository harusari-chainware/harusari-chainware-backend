package com.harusari.chainware.product.query.service;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDetailResponse;
import com.harusari.chainware.product.query.dto.response.ProductListResponse;

public interface ProductQueryService {

    ProductListResponse getProducts(ProductSearchRequest request);

    ProductDetailResponse getProductDetailByAuthority(Long productId, MemberAuthorityType authorityType, int page, int size);
}

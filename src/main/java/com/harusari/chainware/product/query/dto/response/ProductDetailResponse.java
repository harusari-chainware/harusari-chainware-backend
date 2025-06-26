package com.harusari.chainware.product.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductDetailResponse {
    private ProductDto product;
    private List<VendorProductContractDto> contracts;
    private List<VendorDetailDto> vendors;
    private Pagination pagination;
}
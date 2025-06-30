package com.harusari.chainware.product.query.mapper;

import com.harusari.chainware.contract.query.dto.request.VendorByProductRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductQueryMapper {

    Optional<ProductDto> findProductById(Long productId);

    List<VendorProductContractDto> findVendorContractsByProductId(Long productId);

    List<VendorDetailDto> findVendorsByProductId(VendorByProductRequest request);

    long countVendorsByProductId(VendorByProductRequest request);

    List<ProductDto> findProductsByConditions(@Param("request") ProductSearchRequest request);
    long countProductsByConditions(@Param("request") ProductSearchRequest request);

}
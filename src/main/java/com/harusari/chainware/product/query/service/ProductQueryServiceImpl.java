package com.harusari.chainware.product.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.contract.query.dto.request.VendorByProductRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.exception.product.ProductErrorCode;
import com.harusari.chainware.exception.product.ProductNotFoundException;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDetailResponse;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import com.harusari.chainware.product.query.dto.response.ProductListResponse;
import com.harusari.chainware.product.query.mapper.ProductQueryMapper;
import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductQueryMapper productQueryMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductListResponse getProducts(ProductSearchRequest request) {
        List<ProductDto> products = productQueryMapper.findProductsByConditions(request);
        long totalCount = productQueryMapper.countProductsByConditions(request);
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());

        return ProductListResponse.builder()
                .products(products)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalPages(totalPages)
                        .totalItems(totalCount)
                        .build())
                .build();
    }

    @Override
    @Transactional
    public ProductDetailResponse getProductDetailByAuthority(Long productId, MemberAuthorityType authorityType, int page, int size) {
        ProductDto product = productQueryMapper.findProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (Set.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER).contains(authorityType)) {
            VendorByProductRequest vendorRequest = new VendorByProductRequest(productId, page, size);

            List<VendorProductContractDto> contracts = productQueryMapper.findVendorContractsByProductId(vendorRequest);

            List<VendorDetailDto> vendors = productQueryMapper.findVendorsByProductId(vendorRequest);
            long totalCount = productQueryMapper.countVendorsByProductId(vendorRequest);
            int totalPages = (int) Math.ceil((double) totalCount / size);

            return ProductDetailResponse.builder()
                    .product(product)
                    .contracts(contracts)
                    .vendors(vendors)
                    .pagination(Pagination.builder()
                            .currentPage(page)
                            .totalPages(totalPages)
                            .totalItems(totalCount)
                            .build())
                    .build();
        }

        return ProductDetailResponse.builder()
                .product(product)
                .build();
    }
}
package com.harusari.chainware.product.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.contract.query.dto.request.VendorByProductRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.exception.contract.ContractErrorCode;
import com.harusari.chainware.exception.product.ProductErrorCode;
import com.harusari.chainware.exception.product.ProductNotFoundException;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.product.query.dto.request.ProductSearchRequest;
import com.harusari.chainware.product.query.dto.response.ProductDetailResponse;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import com.harusari.chainware.product.query.dto.response.ProductListResponse;
import com.harusari.chainware.product.query.mapper.ProductQueryMapper;
import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[제품 - query service] ProductQueryServiceImpl 테스트")
class ProductQueryServiceImplTest {

    @Mock
    ProductQueryMapper productQueryMapper;

    @InjectMocks
    ProductQueryServiceImpl productQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[제품 목록 조회] 전체 목록 조회 성공")
    void getProducts_success() {
        // given
        ProductSearchRequest request = ProductSearchRequest.builder()
                .page(1).size(10).build();
        List<ProductDto> products = List.of(
                ProductDto.builder().productId(1L).productName("제품A").build(),
                ProductDto.builder().productId(2L).productName("제품B").build()
        );
        given(productQueryMapper.findProductsByConditions(request)).willReturn(products);
        given(productQueryMapper.countProductsByConditions(request)).willReturn(2L);

        // when
        ProductListResponse response = productQueryService.getProducts(request);

        // then
        assertThat(response.getProducts()).hasSize(2);
        assertThat(response.getPagination().getTotalItems()).isEqualTo(2);
    }

    @Nested
    @DisplayName("[제품 상세 조회]")
    class ProductDetail {

        Long productId = 123L;
        int page = 1;
        int size = 10;

        @Test
        @DisplayName("[제품 상세 조회] 관리자 권한자 조회 시 계약 및 거래처 포함")
        void getProductDetail_admin_success() {
            // given
            ProductDto product = ProductDto.builder()
                    .productId(productId)
                    .productName("관리자제품")
                    .build();

            List<VendorProductContractDto> contracts = List.of(
                    VendorProductContractDto.builder().contractId(1L).build()
            );

            List<VendorDetailDto> vendors = List.of(
                    createVendorDetailDto(10L, "벤더A")
            );

            given(productQueryMapper.findProductById(productId)).willReturn(Optional.of(product));
            given(productQueryMapper.findVendorContractsByProductId(productId)).willReturn(contracts);
            given(productQueryMapper.findVendorsByProductId(any(VendorByProductRequest.class))).willReturn(vendors);
            given(productQueryMapper.countVendorsByProductId(any(VendorByProductRequest.class))).willReturn(1L);

            // when
            ProductDetailResponse response = productQueryService.getProductDetailByAuthority(
                    productId, MemberAuthorityType.GENERAL_MANAGER, page, size);

            // then
            assertThat(response.getProduct().getProductId()).isEqualTo(productId);
            assertThat(response.getContracts()).hasSize(1);
            assertThat(response.getVendors()).hasSize(1);
            assertThat(response.getPagination().getTotalItems()).isEqualTo(1L);
        }

        @Test
        @DisplayName("[제품 상세 조회] 가맹점 담당자 조회 시 기본 정보만 반환")
        void getProductDetail_franchisee_success() {
            // given
            ProductDto product = ProductDto.builder()
                    .productId(productId)
                    .productName("담당자제품")
                    .build();

            given(productQueryMapper.findProductById(productId)).willReturn(Optional.of(product));

            // when
            ProductDetailResponse response = productQueryService.getProductDetailByAuthority(
                    productId, MemberAuthorityType.FRANCHISE_MANAGER, page, size);

            // then
            assertThat(response.getProduct().getProductId()).isEqualTo(productId);
            assertThat(response.getContracts()).isNull();
            assertThat(response.getVendors()).isNull();
            assertThat(response.getPagination()).isNull();
        }

        @Test
        @DisplayName("[제품 상세 조회] 존재하지 않는 제품 ID 조회 시 예외 발생")
        void getProductDetail_notFound() {
            // given
            given(productQueryMapper.findProductById(productId)).willReturn(Optional.empty());

            // when/then
            assertThatThrownBy(() ->
                    productQueryService.getProductDetailByAuthority(
                            productId, MemberAuthorityType.GENERAL_MANAGER, page, size
                    )
            ).isInstanceOf(ProductNotFoundException.class);
        }

        private VendorDetailDto createVendorDetailDto(Long vendorId, String vendorName) {
            VendorDetailDto dto = new VendorDetailDto();
            ReflectionTestUtils.setField(dto, "vendorId", vendorId);
            ReflectionTestUtils.setField(dto, "vendorName", vendorName);
            return dto;
        }
    }
}

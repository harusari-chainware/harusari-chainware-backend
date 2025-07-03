package com.harusari.chainware.product.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.harusari.chainware.product.query.dto.response.ProductDetailResponse;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import com.harusari.chainware.product.query.dto.response.ProductListResponse;
import com.harusari.chainware.product.query.service.ProductQueryService;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.vendor.query.dto.VendorDetailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[제품 - query controller] ProductQueryController 테스트")
class ProductQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductQueryService productQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[제품 목록 조회] 성공")
    void testGetProductListSuccess() throws Exception {
        // given
        ProductDto product = ProductDto.builder()
                .productId(1L)
                .productName("감자칩")
                .productCode("PC-001")
                .categoryId(5L)
                .basePrice(2000)
                .unitQuantity("1봉지")
                .unitSpec("100g")
                .storeType(StoreType.ROOM_TEMPERATURE)
                .safetyStock(50)
                .origin("대한민국")
                .shelfLife(180)
                .productStatus(true)
                .productCreatedAt(LocalDateTime.now())
                .productModifiedAt(LocalDateTime.now())
                .build();

        Pagination pagination = Pagination.of(1, 10, 1);
        ProductListResponse response = ProductListResponse.builder()
                .products(List.of(product))
                .pagination(pagination)
                .build();

        Mockito.when(productQueryService.getProducts(any()))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/products")
                        .param("productName", "감자칩")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products[0].productId").value(1L))
                .andExpect(jsonPath("$.data.products[0].productName").value("감자칩"))
                .andExpect(jsonPath("$.data.pagination.currentPage").value(1))
                .andExpect(jsonPath("$.data.pagination.totalPages").value(1))
                .andExpect(jsonPath("$.data.pagination.totalItems").value(1));
    }

    @Test
    @DisplayName("[제품 상세 조회] 성공")
    void testGetProductDetailSuccess() throws Exception {
        // given
        Long productId = 100L;
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        Mockito.when(userDetails.getMemberAuthorityType()).thenReturn(MemberAuthorityType.valueOf("GENERAL_MANAGER"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ProductDto product = ProductDto.builder()
                .productId(productId)
                .productName("감자칩")
                .productCode("PC-001")
                .storeType(StoreType.ROOM_TEMPERATURE)
                .build();

        ProductDetailResponse response = ProductDetailResponse.builder()
                .product(product)
                .contracts(List.of(
                        VendorProductContractDto.builder()
                                .contractId(1L)
                                .vendorName("감자상사")
                                .productName("감자칩")
                                .contractPrice(1800)
                                .build()))
                .vendors(List.of(
                        createVendorDetail(1L, "감자상사")
                ))
                .pagination(Pagination.of(1, 10, 1))
                .build();

        Mockito.when(productQueryService.getProductDetailByAuthority(
                        productId, MemberAuthorityType.valueOf("GENERAL_MANAGER"),1, 10))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.product.productId").value(productId))
                .andExpect(jsonPath("$.data.product.productName").value("감자칩"))
                .andExpect(jsonPath("$.data.contracts[0].vendorName").value("감자상사"))
                .andExpect(jsonPath("$.data.vendors[0].vendorName").value("감자상사"))
                .andExpect(jsonPath("$.data.pagination.totalItems").value(1));

        SecurityContextHolder.clearContext();
    }

    private VendorDetailDto createVendorDetail(Long vendorId, String vendorName) {
        VendorDetailDto dto = new VendorDetailDto();
        try {
            java.lang.reflect.Field f1 = VendorDetailDto.class.getDeclaredField("vendorId");
            java.lang.reflect.Field f2 = VendorDetailDto.class.getDeclaredField("vendorName");
            f1.setAccessible(true);
            f2.setAccessible(true);
            f1.set(dto, vendorId);
            f2.set(dto, vendorName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dto;
    }
}
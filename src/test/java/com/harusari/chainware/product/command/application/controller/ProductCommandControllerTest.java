package com.harusari.chainware.product.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.exception.product.ProductErrorCode;
import com.harusari.chainware.exception.product.ProductNotFoundException;
import com.harusari.chainware.exception.product.handler.ProductExceptionHandler;
import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.application.dto.request.ProductUpdateRequest;
import com.harusari.chainware.product.command.application.dto.response.ProductCommandResponse;
import com.harusari.chainware.product.command.application.service.ProductCommandService;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductCommandController.class)
@Import(ProductExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[제품 - controller] ProductCommandController 테스트")
class ProductCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCommandService productCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductCommandResponse sampleResponse;

    @BeforeEach
    void setUp() {
        // 최소한 productId 정도는 포함해서 빌더 생성
        sampleResponse = ProductCommandResponse.builder()
                .productId(1L)
                // 필요하다면 추가 필드 설정…
                .build();
    }

    @Test
    @DisplayName("[제품 생성] 성공 테스트")
    void testCreateProductSuccess() throws Exception {
        // given
        ProductCreateRequest createReq = ProductCreateRequest.builder()
                .productName("테스트 제품")
                .topCategoryId(2L)
                .categoryId(10L)
                .unitQuantity("1개")
                .unitSpec("10kg")
                .basePrice(1000)
                .storeType(StoreType.ROOM_TEMPERATURE)
                .safetyStock(50)
                .origin("대한민국")
                .shelfLife(365)
                .categoryCode("CAT-001")
                .build();

        when(productCommandService.createProduct(any()))
                .thenReturn(sampleResponse);

        // when & then
        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1));
    }

    @Test
    @DisplayName("[제품 생성] 유효성 검증 실패 테스트")
    void testCreateProductValidationFail() throws Exception {
        // 빈 JSON → @Validated 위반 → 400 Bad Request
        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[제품 수정] 성공 테스트")
    void testUpdateProductSuccess() throws Exception {
        // given
        ProductUpdateRequest updateReq = ProductUpdateRequest.builder()
                .productName("수정된 제품명")
                .basePrice(1200)
                .unitQuantity("2개")
                .unitSpec("20kg")
                .storeType(StoreType.CHILLED)
                .safetyStock(60)
                .origin("미국")
                .shelfLife(180)
                .productStatus(false)
                .build();

        when(productCommandService.updateProduct(eq(1L), any()))
                .thenReturn(sampleResponse);

        // when & then
        mockMvc.perform(put("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1));
    }

    @Test
    @DisplayName("[제품 수정] 존재하지 않는 제품 예외 테스트")
    void testUpdateProductNotFound() throws Exception {
        ProductUpdateRequest updateReq = ProductUpdateRequest.builder()
                // TODO: 실제 DTO 필드에 맞춰 설정
                .build();

        when(productCommandService.updateProduct(eq(1L), any()))
                .thenThrow(new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));

        mockMvc.perform(put("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode")
                        .value(ProductErrorCode.PRODUCT_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[제품 삭제] 성공 테스트")
    void testDeleteProductSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("[제품 삭제] 존재하지 않는 제품 예외 테스트")
    void testDeleteProductNotFound() throws Exception {
        doThrow(new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND))
                .when(productCommandService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode")
                        .value(ProductErrorCode.PRODUCT_NOT_FOUND.getErrorCode()));
    }
}

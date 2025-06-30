package com.harusari.chainware.category.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.category.query.dto.response.CategoryDetailWithProductsResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryListResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryProductPageResponse;
import com.harusari.chainware.category.query.service.CategoryQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryQueryController.class)
@DisplayName("[카테고리 - query controller] CategoryQueryController 테스트")
class CategoryQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryQueryService categoryQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @DisplayName("[전체 카테고리 조회] 전체 카테고리 목록 조회 API 테스트")
    void searchCategories_success() throws Exception {
        TopCategoryListResponse mockResponse = TopCategoryListResponse.builder().build();
        given(categoryQueryService.searchCategories(any())).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/categories")
                        .param("topCategoryName", "식품")
                        .param("categoryName", "과자")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "categoryName")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @DisplayName("[상위 카테고리 조회] 특정 상위 카테고리 제품 목록 조회 API 테스트")
    void getTopCategoryDetailWithProducts_success() throws Exception {
        TopCategoryProductPageResponse mockResponse = TopCategoryProductPageResponse.builder().build();
        given(categoryQueryService.getTopCategoryWithPagedProducts(anyLong(), anyInt(), anyInt()))
                .willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/categories/top/1")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @DisplayName("[카테고리 조회] 특정 카테고리 상세 조회 API 테스트")
    void getCategoryDetail_success() throws Exception {
        CategoryDetailWithProductsResponse mockResponse = CategoryDetailWithProductsResponse.builder().build();
        given(categoryQueryService.getCategoryDetailWithProducts(anyLong(), anyInt(), anyInt()))
                .willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/categories/5")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}

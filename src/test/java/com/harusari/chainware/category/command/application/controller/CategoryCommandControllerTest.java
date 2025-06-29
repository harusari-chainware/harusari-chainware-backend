package com.harusari.chainware.category.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.CategoryCommandService;
import com.harusari.chainware.exception.category.CategoryErrorCode;
import com.harusari.chainware.exception.category.CategoryNotFoundException;
import com.harusari.chainware.exception.category.handler.CategoryExceptionHandler;
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

@WebMvcTest(CategoryCommandController.class)
@Import(CategoryExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[카테고리 - controller] CategoryCommandController 테스트")
class CategoryCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryCommandService categoryCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryCommandResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = CategoryCommandResponse.builder()
                .categoryId(1L)
                .categoryName("테스트카테고리")
                .topCategoryId(10L)
                .categoryCode("CAT-001")
                .build();
    }

    @Test
    @DisplayName("[카테고리 생성] 성공 테스트")
    void testCreateCategorySuccess() throws Exception {
        CategoryCreateRequest createReq = CategoryCreateRequest.builder()
                .categoryName("테스트카테고리")
                .topCategoryId(10L)
                .categoryCode("CAT-001")
                .build();

        when(categoryCommandService.createCategory(any()))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(1))
                .andExpect(jsonPath("$.data.categoryName").value("테스트카테고리"))
                .andExpect(jsonPath("$.data.topCategoryId").value(10))
                .andExpect(jsonPath("$.data.categoryCode").value("CAT-001"));
    }

    @Test
    @DisplayName("[카테고리 생성] 유효성 검증 실패 테스트")
    void testCreateCategoryValidationFail() throws Exception {
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[카테고리 수정] 성공 테스트")
    void testUpdateCategorySuccess() throws Exception {
        CategoryCreateRequest updateReq = CategoryCreateRequest.builder()
                .categoryName("수정된카테고리")
                .topCategoryId(20L)
                .categoryCode("CAT-002")
                .build();

        when(categoryCommandService.updateCategory(eq(1L), any()))
                .thenReturn(sampleResponse);

        mockMvc.perform(put("/api/v1/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(1))
                .andExpect(jsonPath("$.data.categoryName").value("테스트카테고리")); // sampleResponse 기준
    }

    @Test
    @DisplayName("[카테고리 수정] 존재하지 않는 카테고리 예외 테스트")
    void testUpdateCategoryNotFound() throws Exception {
        CategoryCreateRequest updateReq = CategoryCreateRequest.builder()
                .categoryName("수정된카테고리")
                .topCategoryId(20L)
                .categoryCode("CAT-002")
                .build();

        when(categoryCommandService.updateCategory(eq(1L), any()))
                .thenThrow(new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        mockMvc.perform(put("/api/v1/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode")
                        .value(CategoryErrorCode.CATEGORY_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[카테고리 삭제] 성공 테스트")
    void testDeleteCategorySuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("[카테고리 삭제] 존재하지 않는 카테고리 예외 테스트")
    void testDeleteCategoryNotFound() throws Exception {
        doThrow(new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND))
                .when(categoryCommandService).deleteCategory(1L);

        mockMvc.perform(delete("/api/v1/category/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode")
                        .value(CategoryErrorCode.CATEGORY_NOT_FOUND.getErrorCode()));
    }
}
package com.harusari.chainware.category.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;
import com.harusari.chainware.category.command.application.service.CategoryCommandService;
import com.harusari.chainware.exception.category.*;
import com.harusari.chainware.exception.category.handler.CategoryExceptionHandler;
import com.harusari.chainware.securitysupport.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryCommandController.class)
@Import(CategoryExceptionHandler.class)
@DisplayName("[카테고리 - controller] CategoryCommandController 테스트")
class CategoryCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryCommandService categoryCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[카테고리 생성] 성공 테스트")
    @WithMockCustomUser(position = "GENERAL_MANAGER")
    void testCreateCategory_Success() throws Exception {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .categoryName("완제품")
                .topCategoryId(1L)
                .categoryCode("FD")
                .build();

        CategoryCommandResponse response = CategoryCommandResponse.builder()
                .categoryId(10L)
                .topCategoryId(1L)
                .categoryName("음료")
                .build();

        when(categoryCommandService.createCategory(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.categoryId").value(10L));
    }

    @Test
    @DisplayName("[카테고리 생성] 상위 카테고리 없음 예외 테스트")
    @WithMockCustomUser(memberId = 100L, position = "GENERAL_MANAGER")
    void testCreateCategory_TopCategoryNotFound() throws Exception {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .categoryName("완제품")
                .topCategoryId(1L)
                .categoryCode("FD")
                .build();

        when(categoryCommandService.createCategory(any()))
                .thenThrow(new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND));

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[카테고리 생성] 중복 이름 예외 테스트")
    @WithMockCustomUser(position = "GENERAL_MANAGER")
    void testCreateCategory_DuplicateName() throws Exception {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .categoryName("완제품")
                .topCategoryId(1L)
                .categoryCode("FD")
                .build();

        when(categoryCommandService.createCategory(any()))
                .thenThrow(new CategoryNameAlreadyExistsException(CategoryErrorCode.CATEGORY_NAME_ALREADY_EXISTS));

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value(CategoryErrorCode.CATEGORY_NAME_ALREADY_EXISTS.getErrorCode()));
    }

    @Test
    @DisplayName("[카테고리 수정] 중복 이름 예외 테스트")
    @WithMockCustomUser(position = "GENERAL_MANAGER")
    void testUpdateCategory_DuplicateName() throws Exception {
        Long categoryId = 10L;
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .categoryName("중복이름")
                .topCategoryId(1L)
                .categoryCode("FD")
                .build();

        when(categoryCommandService.updateCategory(eq(categoryId), any()))
                .thenThrow(new TopCategoryNameAlreadyExistsException(CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS));

        mockMvc.perform(put("/api/v1/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value(CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS.getErrorCode()));
    }

    @Test
    @DisplayName("[카테고리 삭제] 삭제 불가 - 연결된 제품 있음")
    @WithMockCustomUser(position = "GENERAL_MANAGER")
    void testDeleteCategory_HasProducts() throws Exception {
        Long categoryId = 5L;

        doThrow(new CategoryCannotDeleteHasProductsException(CategoryErrorCode.CATEGORY_CANNOT_DELETE_HAS_PRODUCTS))
                .when(categoryCommandService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/v1/category/{categoryId}", categoryId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value(CategoryErrorCode.CATEGORY_CANNOT_DELETE_HAS_PRODUCTS.getErrorCode()));
    }

    @Test
    @DisplayName("[카테고리 삭제] 성공 테스트")
    @WithMockCustomUser(position = "GENERAL_MANAGER")
    void testDeleteCategory_Success() throws Exception {
        Long categoryId = 10L;
        doNothing().when(categoryCommandService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/v1/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
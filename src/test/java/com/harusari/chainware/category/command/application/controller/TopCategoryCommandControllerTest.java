//package com.harusari.chainware.category.command.application.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
//import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
//import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
//import com.harusari.chainware.category.command.application.service.TopCategoryCommandService;
//import com.harusari.chainware.exception.category.CategoryErrorCode;
//import com.harusari.chainware.exception.category.TopCategoryNotFoundException;
//import com.harusari.chainware.exception.category.handler.CategoryExceptionHandler;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(TopCategoryCommandController.class)
//@Import(CategoryExceptionHandler.class)
//@AutoConfigureMockMvc(addFilters = false)
//@DisplayName("[상위 카테고리 - controller] TopCategoryCommandController 테스트")
//class TopCategoryCommandControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TopCategoryCommandService topCategoryCommandService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private TopCategoryCommandResponse sampleResponse;
//
//    @BeforeEach
//    void setUp() {
//        sampleResponse = TopCategoryCommandResponse.builder()
//                .topCategoryId(1L)
//                .topCategoryName("테스트상위카테고리")
//                .build();
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 생성] 성공 테스트")
//    void testCreateSuccess() throws Exception {
//        TopCategoryCreateRequest createReq = TopCategoryCreateRequest.builder()
//                .topCategoryName("테스트상위카테고리")
//                .build();
//
//        when(topCategoryCommandService.createTopCategory(any()))
//                .thenReturn(sampleResponse);
//
//        mockMvc.perform(post("/api/v1/topcategory")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.topCategoryId").value(1))
//                .andExpect(jsonPath("$.data.topCategoryName").value("테스트상위카테고리"));
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 생성] 유효성 검증 실패 테스트")
//    void testCreateValidationFail() throws Exception {
//        // 빈 JSON 으로 요청 시 @NotBlank 위반 → 400
//        mockMvc.perform(post("/api/v1/topcategory")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 수정] 성공 테스트")
//    void testUpdateSuccess() throws Exception {
//        TopCategoryUpdateRequest updateReq = new TopCategoryUpdateRequest("수정된상위카테고리");
//
//        when(topCategoryCommandService.updateTopCategory(eq(1L), any()))
//                .thenReturn(sampleResponse);
//
//        mockMvc.perform(put("/api/v1/topcategory/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.topCategoryId").value(1))
//                .andExpect(jsonPath("$.data.topCategoryName").value("테스트상위카테고리")); // sampleResponse 기준
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 수정] 존재하지 않는 상위 카테고리 예외 테스트")
//    void testUpdateNotFound() throws Exception {
//        TopCategoryUpdateRequest updateReq = new TopCategoryUpdateRequest("수정된상위카테고리");
//
//        when(topCategoryCommandService.updateTopCategory(eq(1L), any()))
//                .thenThrow(new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND));
//
//        mockMvc.perform(put("/api/v1/topcategory/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateReq)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.errorCode")
//                        .value(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND.getErrorCode()));
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 삭제] 성공 테스트")
//    void testDeleteSuccess() throws Exception {
//        mockMvc.perform(delete("/api/v1/topcategory/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isEmpty());
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 삭제] 존재하지 않는 상위 카테고리 예외 테스트")
//    void testDeleteNotFound() throws Exception {
//        doThrow(new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND))
//                .when(topCategoryCommandService).deleteTopCategory(1L);
//
//        mockMvc.perform(delete("/api/v1/topcategory/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.errorCode")
//                        .value(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND.getErrorCode()));
//    }
//}
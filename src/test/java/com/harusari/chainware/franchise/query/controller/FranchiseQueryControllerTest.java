package com.harusari.chainware.franchise.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.service.FranchiseQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.harusari.chainware.exception.franchise.FranchiseErrorCode.FRANCHISE_NOT_FOUND_EXCEPTION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FranchiseQueryController.class)
@DisplayName("[가맹점 - controller] FranchiseQueryController 테스트")
class FranchiseQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FranchiseQueryService franchiseQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = "SENIOR_MANAGER")
    @Test
    @DisplayName("[가맹점 조회] 페이징 조회 API 호출 시 200 OK 및 표준 응답 검증")
    void testSearchFranchises() throws Exception {
        FranchiseSearchResponse franchise1 = FranchiseSearchResponse.builder()
                .franchiseName("가맹점01")
                .franchiseContact("01012341234")
                .build();

        FranchiseSearchResponse franchise2 = FranchiseSearchResponse.builder()
                .franchiseName("가맹점02")
                .franchiseContact("01056785678")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<FranchiseSearchResponse> mockPage = new PageImpl<>(List.of(franchise1, franchise2), pageable, 2);

        Mockito.when(franchiseQueryService.searchFranchises(any(FranchiseSearchRequest.class), any(Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/franchises")
                        .param("franchiseName", "가맹점")
                        .param("zipcode", "12345")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.contents").isArray())
                .andExpect(jsonPath("$.data.contents[0].franchiseName").value("가맹점01"))
                .andExpect(jsonPath("$.data.contents[1].franchiseName").value("가맹점02"))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @WithMockUser(username = "admin", roles = "SENIOR_MANAGER")
    @Test
    @DisplayName("[가맹점 상세 조회] 상세 조회 API 호출 시 200 OK 및 표준 응답 검증")
    void testSearchFranchise() throws Exception {
        Long franchiseId = 1L;

        FranchiseSearchDetailResponse detailResponse = FranchiseSearchDetailResponse.builder()
                .franchiseId(franchiseId)
                .franchiseName("가맹점01")
                .franchiseContact("01012341234")
                .franchiseTaxId("1234567890")
                .build();

        Mockito.when(franchiseQueryService.getFranchiseDetail(eq(franchiseId)))
                .thenReturn(detailResponse);

        mockMvc.perform(get("/api/v1/franchises/{franchiseId}", franchiseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.franchiseId").value(franchiseId))
                .andExpect(jsonPath("$.data.franchiseName").value("가맹점01"))
                .andExpect(jsonPath("$.data.franchiseContact").value("01012341234"))
                .andExpect(jsonPath("$.data.franchiseTaxId").value("1234567890"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(franchiseQueryService).getFranchiseDetail(eq(franchiseId));
    }


    @WithMockUser(username = "admin", roles = "SENIOR_MANAGER")
    @Test
    @DisplayName("[가맹점 상세 조회] 존재하지 않는 ID로 조회 시 404 및 표준 에러 응답 검증")
    void testSearchFranchise_NotFound() throws Exception {
        // given
        Long invalidFranchiseId = 999L;

        Mockito.when(franchiseQueryService.getFranchiseDetail(eq(invalidFranchiseId)))
                .thenThrow(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND_EXCEPTION));

        // when & then
        mockMvc.perform(get("/api/v1/franchises/{franchiseId}", invalidFranchiseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(FRANCHISE_NOT_FOUND_EXCEPTION.getErrorCode()))
                .andExpect(jsonPath("$.message").value(FRANCHISE_NOT_FOUND_EXCEPTION.getErrorMessage()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(franchiseQueryService).getFranchiseDetail(eq(invalidFranchiseId));
    }

}
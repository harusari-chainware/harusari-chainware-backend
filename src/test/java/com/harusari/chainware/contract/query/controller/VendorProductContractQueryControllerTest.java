package com.harusari.chainware.contract.query.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListDto;
import com.harusari.chainware.contract.query.service.VendorProductContractService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[계약 조회 - controller] VendorProductContractQueryController 테스트")
@WebMvcTest(VendorProductContractQueryController.class)
class VendorProductContractQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorProductContractService contractService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails mockUser(boolean isManager) {
        return new CustomUserDetails(
                1L,
                "test@email.com",
                isManager ? MemberAuthorityType.GENERAL_MANAGER : MemberAuthorityType.VENDOR_MANAGER
        );
    }

    @Test
    @DisplayName("[계약 목록 조회] 관리자 권한으로 계약 목록 조회 성공")
    void getContracts_asManager_success() throws Exception {
        // given
        VendorProductContractListDto dto = VendorProductContractListDto.builder()
                .contractId(1L)
                .vendorName("ABC유통")
                .productName("아메리카노")
                .basePrice(1000)
                .contractPrice(900)
                .minOrderQty(10)
                .leadTime(3)
                .contractStatus(ContractStatus.ACTIVE)
                .contractStartDate(LocalDate.of(2025, 1, 1))
                .contractEndDate(LocalDate.of(2025, 12, 31))
                .build();

        PagedResult<VendorProductContractListDto> result = PagedResult.<VendorProductContractListDto>builder()
                .content(List.of(dto))
                .pagination(PagedResult.PaginationMeta.builder()
                        .page(1)
                        .size(10)
                        .totalElements(1L)
                        .totalPages(1)
                        .build())
                .build();

        given(contractService.getContracts(any(), anyLong(), eq(true)))
                .willReturn(result);

        // when & then
        mockMvc.perform(get("/api/v1/contracts")
                        .param("productName", "아메리카노")
                        .param("page", "1")
                        .param("size", "10")
                        .with(user(mockUser(true)))) // 관리자 권한 유저
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].contractId").value(1L))
                .andExpect(jsonPath("$.data.content[0].vendorName").value("ABC유통"))
                .andExpect(jsonPath("$.data.content[0].productName").value("아메리카노"));
    }

    @Test
    @DisplayName("[계약 목록 조회] 벤더 권한으로 계약 목록 조회 성공")
    void getContracts_asVendor_success() throws Exception {
        // given
        PagedResult<VendorProductContractListDto> result = PagedResult.<VendorProductContractListDto>builder()
                .content(List.of()) // 비어 있는 계약 목록
                .pagination(PagedResult.PaginationMeta.builder()
                        .page(1)
                        .size(10)
                        .totalElements(0L)
                        .totalPages(0)
                        .build())
                .build();

        given(contractService.getContracts(any(), anyLong(), eq(false)))
                .willReturn(result);

        // when & then
        mockMvc.perform(get("/api/v1/contracts")
                        .param("vendorName", "ABC유통")
                        .param("page", "1")
                        .param("size", "10")
                        .with(user(mockUser(false)))) // 벤더 권한 유저
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(0));
    }
    @Test
    @DisplayName("[단일 계약 조회] 관리자 권한으로 단일 계약 조회 성공")
    void getContract_asManager_success() throws Exception {
        // given
        VendorProductContractDto mockDto = VendorProductContractDto.builder()
                .contractId(100L)
                .productName("콜드브루")
                .build();

        given(contractService.getContractById(eq(100L), anyLong(), eq(true)))
                .willReturn(mockDto);

        // when & then
        mockMvc.perform(get("/api/v1/contracts/100")
                        .with(user(mockUser(true))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contractId").value(100L));
    }

    @Test
    @DisplayName("[단일 계약 조회] 벤더 권한으로 단일 계약 조회 성공")
    void getContract_asVendor_success() throws Exception {
        VendorProductContractDto mockDto = VendorProductContractDto.builder()
                .contractId(200L)
                .productName("라떼")
                .build();

        given(contractService.getContractById(eq(200L), anyLong(), eq(false)))
                .willReturn(mockDto);

        mockMvc.perform(get("/api/v1/contracts/200")
                        .with(user(mockUser(false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contractId").value(200L));
    }
}

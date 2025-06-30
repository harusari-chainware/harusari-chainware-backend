//package com.harusari.chainware.contract.command.application.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
//import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
//import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;
//import com.harusari.chainware.contract.command.application.service.ContractService;
//import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
//import com.harusari.chainware.exception.contract.ContractErrorCode;
//import com.harusari.chainware.exception.contract.ContractNotFoundException;
//import com.harusari.chainware.exception.contract.handler.ContractExceptionHandler;
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
//import java.time.LocalDate;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ContractCommandController.class)
//@Import(ContractExceptionHandler.class)
//@AutoConfigureMockMvc(addFilters = false)
//@DisplayName("[계약 - controller] ContractCommandController 테스트")
//class ContractCommandControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ContractService contractService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ContractResponse sampleResponse;
//
//    @BeforeEach
//    void setUp() {
//        sampleResponse = ContractResponse.builder()
//                .contractId(42L)
//                .contractId(42L)
//                .productId(100L)
//                .vendorId(200L)
//                .contractPrice(50000)
//                .minOrderQty(10)
//                .leadTime(7)
//                .contractStartDate(LocalDate.of(2025, 1, 1))
//                .contractEndDate(LocalDate.of(2025, 12, 31))
//                .contractStatus(ContractStatus.ACTIVE)
//                .build();
//    }
//
//    @Test
//    @DisplayName("[계약 생성] 성공 테스트")
//    void testCreateContractSuccess() throws Exception {
//        // given
//        ContractCreateRequest createReq = ContractCreateRequest.builder()
//                .productId(100L)
//                .vendorId(200L)
//                .contractPrice(50000)
//                .minOrderQty(10)
//                .leadTime(7)
//                .contractStartDate(LocalDate.of(2025, 1, 1))
//                .contractEndDate(LocalDate.of(2025, 12, 31))
//                .contractStatus(ContractStatus.ACTIVE)
//                .build();
//
//        when(contractService.createContract(any()))
//                .thenReturn(sampleResponse);
//
//        // when & then
//        mockMvc.perform(post("/api/v1/contract")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.contractId").value(42));
//    }
//
//    @Test
//    @DisplayName("[계약 생성] 유효성 검증 실패 테스트")
//    void testCreateContractValidationFail() throws Exception {
//        String emptyJson = "{}";
//
//        mockMvc.perform(post("/api/v1/contract")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(emptyJson))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("[계약 수정] 성공 테스트")
//    void testUpdateContractSuccess() throws Exception {
//        // given
//        ContractUpdateRequest updateReq = ContractUpdateRequest.builder()
//                .productId(101L)
//                .vendorId(201L)
//                .contractPrice(55000)
//                .minOrderQty(5)
//                .leadTime(3)
//                .contractStartDate(LocalDate.of(2025, 2, 1))
//                .contractEndDate(LocalDate.of(2025, 11, 30))
//                .contractStatus(ContractStatus.EXPIRED)
//                .build();
//
//        when(contractService.updateContract(eq(42L), any()))
//                .thenReturn(sampleResponse);
//
//        // when & then
//        mockMvc.perform(put("/api/v1/contract/42")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateReq)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.contractId").value(42));
//    }
//
//    @Test
//    @DisplayName("[계약 수정] 존재하지 않는 계약 예외 테스트")
//    void testUpdateContractNotFound() throws Exception {
//        // given
//        ContractUpdateRequest updateReq = ContractUpdateRequest.builder().build();
//        when(contractService.updateContract(eq(42L), any()))
//                .thenThrow(new ContractNotFoundException(ContractErrorCode.CONTRACT_NOT_FOUND));
//        // when & then
//        mockMvc.perform(put("/api/v1/contract/42")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateReq)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.errorCode").value(ContractErrorCode.CONTRACT_NOT_FOUND.getErrorCode()));
//    }
//
//    @Test
//    @DisplayName("[계약 삭제] 성공 테스트")
//    void testDeleteContractSuccess() throws Exception {
//        // when & then
//        mockMvc.perform(delete("/api/v1/contract/42"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isEmpty());
//    }
//
//    @Test
//    @DisplayName("[계약 삭제] 존재하지 않는 계약 예외 테스트")
//    void testDeleteContractNotFound() throws Exception {
//        // given
//        doThrow(new ContractNotFoundException(ContractErrorCode.CONTRACT_NOT_FOUND))
//                .when(contractService).deleteContract(42L);
//
//        // when & then
//        mockMvc.perform(delete("/api/v1/contract/42"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.errorCode").value(ContractErrorCode.CONTRACT_NOT_FOUND.getErrorCode()));
//    }
//}
package com.harusari.chainware.franchise.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.franchise.command.application.dto.request.UpdateFranchiseRequest;
import com.harusari.chainware.franchise.command.application.service.FranchiseCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FranchiseCommandController.class)
@DisplayName("[가맹점 - controller] FranchiseCommandController 테스트")
class FranchiseCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FranchiseCommandService franchiseCommandService;

    @WithMockUser(username = "admin", roles = "SENIOR_MANAGER")
    @Test
    @DisplayName("[가맹점 수정] 수정 API 호출 시 200 OK 및 표준 응답 검증")
    void testUpdateFranchise() throws Exception {
        Long franchiseId = 1L;

        UpdateFranchiseRequest request = UpdateFranchiseRequest.builder()
                .franchiseName("가맹점 수정")
                .franchiseContact("01012341234")
                .franchiseTaxId("1112223334")
                .franchiseStatus(null)
                .addressRequest(null)
                .build();

        MockMultipartFile jsonPart = new MockMultipartFile(
                "updateFranchiseRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile filePart = new MockMultipartFile(
                "agreementFile",
                "contract-update.pdf",
                "application/pdf",
                "dummy data".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/franchises/{franchiseId}", franchiseId)
                        .file(jsonPart)
                        .file(filePart)
                        .with(requestBuilder -> {
                            requestBuilder.setMethod("PUT");
                            return requestBuilder;
                        })
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(franchiseCommandService)
                .updateFranchise(eq(franchiseId), any(UpdateFranchiseRequest.class), any());
    }

}
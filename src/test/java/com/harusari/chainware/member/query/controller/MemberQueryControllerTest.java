package com.harusari.chainware.member.query.controller;

import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.service.MemberQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberQueryController.class)
@DisplayName("[회원 - query controller] MemberQueryController 테스트")
class MemberQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberQueryService memberQueryService;

    private EmailExistsResponse emailExistsResponse;

    @WithMockUser(username = "admin", roles = "MASTER")
    @Test
    @DisplayName("[회원 이메일 중복O] 회원가입 시 중복된 이메일이면 토큰을 발급하지 않는 테스트")
    void testCheckEmailDuplicateNotToken() throws Exception {
        String email = "test@harusari.com";
        emailExistsResponse = EmailExistsResponse.builder()
                .exists(true)
                .validationToken(null)
                .build();

        when(memberQueryService.checkEmailDuplicate(email)).thenReturn(emailExistsResponse);

        mockMvc.perform(get("/api/v1/members/email-exists")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.exists").exists())
                .andExpect(jsonPath("$.data.exists").value(true))
                .andExpect(jsonPath("$.data.validationToken").doesNotExist())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @WithMockUser(username = "admin", roles = "MASTER")
    @Test
    @DisplayName("[회원 이메일 중복X] 회원가입 시 중복되지 않은 이메일임녀 토큰을 발급하는 테스트")
    void testCheckEmailDuplicateToken() throws Exception {
        String email = "test@harusari.com";
        String token = UUID.randomUUID().toString();
        emailExistsResponse = EmailExistsResponse.builder()
                .exists(false)
                .validationToken(token)
                .build();

        when(memberQueryService.checkEmailDuplicate(email)).thenReturn(emailExistsResponse);

        mockMvc.perform(get("/api/v1/members/email-exists")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.exists").exists())
                .andExpect(jsonPath("$.data.exists").value(false))
                .andExpect(jsonPath("$.data.validationToken").exists())
                .andExpect(jsonPath("$.data.validationToken").value(token))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

}
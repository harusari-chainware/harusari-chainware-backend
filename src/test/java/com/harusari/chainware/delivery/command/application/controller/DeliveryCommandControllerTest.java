package com.harusari.chainware.delivery.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.application.service.DeliveryCommandService;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.exception.DeliveryErrorCode;
import com.harusari.chainware.delivery.exception.DeliveryException;
import com.harusari.chainware.delivery.exception.handler.DeliveryExceptionHandler;
import com.harusari.chainware.securitysupport.WithMockCustomUser;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryCommandController.class)
@Import(DeliveryExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[배송 - controller] DeliveryCommandController 테스트")
class DeliveryCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryCommandService deliveryCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    DeliveryCommandResponse response;

    @BeforeEach
    void setUp() {
        response = DeliveryCommandResponse.builder()
                .deliveryId(1L)
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .build();
    }

    @Test
    @DisplayName("[배송 시작] 성공 테스트")
    @WithMockCustomUser(memberId = 100L, position = "WAREHOUSE_MANAGER")
    void testStartDeliverySuccess() throws Exception {
        // given
        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .build();

        when(deliveryCommandService.startDelivery(eq(1L), any(DeliveryStartRequest.class), eq(100L)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/delivery/1/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.deliveryId").value(1L))
                .andExpect(jsonPath("$.data.deliveryStatus").value("IN_TRANSIT"));
    }

    @Test
    @DisplayName("[배송 시작] 존재하지 않는 배송 ID 예외 테스트")
    @WithMockCustomUser(memberId = 100L, position = "WAREHOUSE_MANAGER")
    void testStartDelivery_NotFound() throws Exception {
        // given
        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .build();

        when(deliveryCommandService.startDelivery(eq(1L), any(DeliveryStartRequest.class), eq(100L)))
                .thenThrow(new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        // when & then
        mockMvc.perform(put("/api/v1/delivery/1/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(DeliveryErrorCode.DELIVERY_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[배송 완료] 성공 테스트")
    @WithMockCustomUser(memberId = 200L, position = "FRANCHISE_MANAGER")
    void testCompleteDeliverySuccess() throws Exception {
        // given
        DeliveryCommandResponse completeResponse = DeliveryCommandResponse.builder()
                .deliveryId(1L)
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .build();

        when(deliveryCommandService.completeDelivery(1L, 200L))
                .thenReturn(completeResponse);

        // when & then
        mockMvc.perform(put("/api/v1/delivery/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.deliveryId").value(1L))
                .andExpect(jsonPath("$.data.deliveryStatus").value("DELIVERED"));
    }

    @Test
    @DisplayName("[배송 완료] 상태가 '배송 중'이 아님 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "FRANCHISE_MANAGER")
    void testCompleteDelivery_InvalidStatus() throws Exception {
        // given
        when(deliveryCommandService.completeDelivery(1L, 200L))
                .thenThrow(new DeliveryException(DeliveryErrorCode.DELIVERY_STATUS_NOT_IN_TRANSIT));

        // when & then
        mockMvc.perform(put("/api/v1/delivery/1/complete"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(DeliveryErrorCode.DELIVERY_STATUS_NOT_IN_TRANSIT.getErrorCode()));
    }
}

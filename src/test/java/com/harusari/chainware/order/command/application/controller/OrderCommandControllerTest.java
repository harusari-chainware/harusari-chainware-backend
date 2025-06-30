package com.harusari.chainware.order.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.order.command.application.dto.request.*;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.application.service.OrderCommandService;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderException;
import com.harusari.chainware.order.exception.handler.OrderExceptionHandler;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderCommandController.class)
@Import(OrderExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[주문 - controller] OrderCommandController 테스트")
class OrderCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderCommandService orderCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    OrderCreateRequest validRequest;
    OrderCommandResponse response;

    @BeforeEach
    void setUp() {
        validRequest = OrderCreateRequest.builder()
                .franchiseId(1L)
                .deliveryDueDate(LocalDate.now().plusDays(3))
                .orderDetails(List.of(
                        OrderDetailCreateRequest.builder()
                                .productId(1L)
                                .quantity(3)
                                .build(),
                        OrderDetailCreateRequest.builder()
                                .productId(2L)
                                .quantity(2)
                                .build()
                ))
                .build();

        response = OrderCommandResponse.builder()
                .orderId(1L)
                .franchiseId(1L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("[주문 등록] 성공 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testCreateOrderSuccess() throws Exception {
        // when
        when(orderCommandService.createOrder(any(), eq(100L))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(response.getOrderId()))
                .andExpect(jsonPath("$.data.franchiseId").value(response.getFranchiseId()));
    }

    @Test
    @DisplayName("[주문 등록] 재고 락 획득 실패 예외 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testCreateOrder_LockFail() throws Exception {
        // when
        when(orderCommandService.createOrder(any(), anyLong()))
                .thenThrow(new OrderException(OrderErrorCode.INVENTORY_LOCK_TIMEOUT));

        // then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.INVENTORY_LOCK_TIMEOUT.getErrorCode()));
    }

    @Test
    @DisplayName("[주문 등록] 빈 주문 상세 예외 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testCreateOrder_EmptyDetails() throws Exception {
        // given
        OrderCreateRequest requestWithEmptyDetails = OrderCreateRequest.builder()
                .franchiseId(validRequest.getFranchiseId())
                .deliveryDueDate(validRequest.getDeliveryDueDate())
                .orderDetails(List.of())  // 빈 리스트 전달
                .build();

        // when
        when(orderCommandService.createOrder(any(), anyLong()))
                .thenThrow(new OrderException(OrderErrorCode.EMPTY_ORDER_DETAIL));

        // then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithEmptyDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.EMPTY_ORDER_DETAIL.getErrorCode()));
    }

    @Test
    @DisplayName("[주문 수정] 성공 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testUpdateOrderSuccess() throws Exception {
        // given
        Long orderId = 1L;
        OrderUpdateRequest updateRequest = OrderUpdateRequest.builder()
                .deliveryDueDate(LocalDate.now().plusDays(5))
                .orderDetails(List.of(
                        OrderDetailUpdateRequest.builder()
                                .productId(1L)
                                .quantity(5)
                                .build(),
                        OrderDetailUpdateRequest.builder()
                                .productId(2L)
                                .quantity(1)
                                .build()
                ))
                .build();

        OrderCommandResponse updatedResponse = OrderCommandResponse.builder()
                .orderId(orderId)
                .franchiseId(1L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        // when
        when(orderCommandService.updateOrder(eq(orderId), any(OrderUpdateRequest.class), eq(100L)))
                .thenReturn(updatedResponse);

        // then
        mockMvc.perform(put("/api/v1/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(orderId));
    }

    @Test
    @DisplayName("[주문 수정] 수정 불가 상태 예외 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testUpdateOrder_CannotUpdateStatus() throws Exception {
        // given
        Long orderId = 1L;
        OrderUpdateRequest updateRequest = OrderUpdateRequest.builder()
                .deliveryDueDate(LocalDate.now().plusDays(5))
                .orderDetails(List.of(
                        OrderDetailUpdateRequest.builder()
                                .productId(1L)
                                .quantity(3)
                                .build()
                ))
                .build();

        // when
        when(orderCommandService.updateOrder(eq(orderId), any(OrderUpdateRequest.class), eq(100L)))
                .thenThrow(new OrderException(OrderErrorCode.CANNOT_UPDATE_ORDER_STATUS));

        // then
        mockMvc.perform(put("/api/v1/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.CANNOT_UPDATE_ORDER_STATUS.getErrorCode()));
    }

    @Test
    @DisplayName("[주문 취소] 성공 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testCancelOrderSuccess() throws Exception {
        // given
        Long orderId = 1L;

        OrderCommandResponse cancelResponse = OrderCommandResponse.builder()
                .orderId(orderId)
                .franchiseId(1L)
                .orderStatus(OrderStatus.CANCELLED)
                .createdAt(LocalDateTime.now())
                .build();

        // when
        when(orderCommandService.cancelOrder(eq(orderId), eq(100L)))
                .thenReturn(cancelResponse);

        // then
        mockMvc.perform(put("/api/v1/orders/{orderId}/cancel", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(orderId))
                .andExpect(jsonPath("$.data.orderStatus").value("CANCELLED"));
    }

    @Test
    @DisplayName("[주문 취소] 취소 불가 상태 예외 테스트")
    @WithMockCustomUser(memberId = 100L, position = "FRANCHISE_MANAGER")
    void testCancelOrder_CannotCancelStatus() throws Exception {
        // given
        Long orderId = 1L;

        // when
        when(orderCommandService.cancelOrder(eq(orderId), eq(100L)))
                .thenThrow(new OrderException(OrderErrorCode.CANNOT_CANCEL_ORDER));

        // then
        mockMvc.perform(put("/api/v1/orders/{orderId}/cancel", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.CANNOT_CANCEL_ORDER.getErrorCode()));
    }

    @Test
    @DisplayName("[주문 승인] 성공 테스트 - 일반 관리자")
    @WithMockCustomUser(memberId = 200L, position = "GENERAL_MANAGER")
    void testApproveOrderSuccessAsGeneralManager() throws Exception {
        // given
        Long orderId = 1L;
        Long warehouseId = 10L;

        OrderCommandResponse approveResponse = OrderCommandResponse.builder()
                .orderId(orderId)
                .franchiseId(1L)
                .orderStatus(OrderStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();

        OrderApproveRequest request = OrderApproveRequest.builder()
                .warehouseId(warehouseId)
                .build();

        // when
        when(orderCommandService.approveOrder(eq(orderId), any(OrderApproveRequest.class), eq(200L)))
                .thenReturn(approveResponse);

        // then
        mockMvc.perform(put("/api/v1/orders/{orderId}/approve", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderStatus").value("APPROVED"));
    }

    @Test
    @DisplayName("[주문 승인] 성공 테스트 - 책임 관리자")
    @WithMockCustomUser(memberId = 300L, position = "SENIOR_MANAGER")
    void testApproveOrderSuccessAsSeniorManager() throws Exception {
        // given
        Long orderId = 1L;
        Long warehouseId = 10L;

        OrderCommandResponse approveResponse = OrderCommandResponse.builder()
                .orderId(orderId)
                .franchiseId(1L)
                .orderStatus(OrderStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();

        OrderApproveRequest request = OrderApproveRequest.builder()
                .warehouseId(warehouseId)
                .build();

        when(orderCommandService.approveOrder(eq(orderId), any(OrderApproveRequest.class), eq(300L)))
                .thenReturn(approveResponse);

        // when & then
        mockMvc.perform(put("/api/v1/orders/{orderId}/approve", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderStatus").value("APPROVED"));
    }


    @Test
    @DisplayName("[주문 승인] 승인 불가 상태 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "GENERAL_MANAGER")
    void testApproveOrder_CannotApprove() throws Exception {
        // given
        Long orderId = 1L;
        Long warehouseId = 99L;

        OrderApproveRequest request = OrderApproveRequest.builder()
                .warehouseId(warehouseId)
                .build();

        when(orderCommandService.approveOrder(eq(orderId), any(OrderApproveRequest.class), eq(200L)))
                .thenThrow(new OrderException(OrderErrorCode.CANNOT_APPROVE_ORDER));

        // when & then
        mockMvc.perform(put("/api/v1/orders/{orderId}/approve", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.CANNOT_APPROVE_ORDER.getErrorCode()));
    }


    @Test
    @DisplayName("[주문 반려] 성공 테스트")
    @WithMockCustomUser(memberId = 200L, position = "GENERAL_MANAGER")
    void testRejectOrderSuccess() throws Exception {
        // given
        Long orderId = 1L;
        OrderRejectRequest rejectRequest = OrderRejectRequest.builder()
                .rejectReason("요청 수량 오류")
                .build();

        OrderCommandResponse rejectResponse = OrderCommandResponse.builder()
                .orderId(orderId)
                .franchiseId(1L)
                .orderStatus(OrderStatus.REJECTED)
                .createdAt(LocalDateTime.now())
                .build();

        when(orderCommandService.rejectOrder(eq(orderId), any(OrderRejectRequest.class), eq(200L)))
                .thenReturn(rejectResponse);

        // when & then
        mockMvc.perform(put("/api/v1/orders/{orderId}/reject", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rejectRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderStatus").value("REJECTED"));
    }

    @Test
    @DisplayName("[주문 반려] 반려 사유 누락 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "GENERAL_MANAGER")
    void testRejectOrder_MissingReason() throws Exception {
        // given
        Long orderId = 1L;
        OrderRejectRequest rejectRequest = OrderRejectRequest.builder()
                .rejectReason("  ")  // 빈 값 처리
                .build();

        when(orderCommandService.rejectOrder(eq(orderId), any(OrderRejectRequest.class), eq(200L)))
                .thenThrow(new OrderException(OrderErrorCode.REJECT_REASON_REQUIRED));

        // when & then
        mockMvc.perform(put("/api/v1/orders/{orderId}/reject", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rejectRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.REJECT_REASON_REQUIRED.getErrorCode()));
    }

    @Test
    @DisplayName("[주문 반려] 반려 불가 상태 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "GENERAL_MANAGER")
    void testRejectOrder_InvalidStatus() throws Exception {
        // given
        Long orderId = 1L;
        OrderRejectRequest rejectRequest = OrderRejectRequest.builder()
                .rejectReason("사유 있음")
                .build();

        when(orderCommandService.rejectOrder(eq(orderId), any(OrderRejectRequest.class), eq(200L)))
                .thenThrow(new OrderException(OrderErrorCode.CANNOT_REJECT_ORDER));

        // when & then
        mockMvc.perform(put("/api/v1/orders/{orderId}/reject", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rejectRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(OrderErrorCode.CANNOT_REJECT_ORDER.getErrorCode()));
    }

}


package com.harusari.chainware.warehouse.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.application.service.WarehouseCommandService;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import com.harusari.chainware.warehouse.exception.handler.WarehouseExceptionHandler;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarehouseCommandController.class)
@Import(WarehouseExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("[창고 - controller] WarehouseCommandController 테스트")
class WarehouseCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseCommandService warehouseCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    private Address address;
    private AddressRequest addressRequest;

    private WarehouseUpdateRequest updateRequest;
    private WarehouseCommandResponse warehouseResponse;
    private WarehouseInventoryCommandResponse inventoryResponse;

    @BeforeEach
    void setUp() {
        addressRequest = AddressRequest.builder()
                .zipcode("67890")
                .addressRoad("New Address")
                .addressDetail("New Address Detail")
                .build();

        address = Address.builder()
                .zipcode("12345")
                .addressRoad("Updated Address")
                .addressDetail("Updated Address Detail")
                .build();

        updateRequest = WarehouseUpdateRequest.builder()
                .warehouseName("Updated Name")
                .warehouseAddress(addressRequest)
                .warehouseStatus(true)
                .build();

        warehouseResponse = WarehouseCommandResponse.builder()
                .warehouseId(1L)
                .warehouseName("Updated Name")
                .warehouseAddress(address)
                .warehouseStatus(true)
                .build();

        inventoryResponse = WarehouseInventoryCommandResponse.builder()
                .inventoryId(1L)
                .productId(1L)
                .quantity(100)
                .build();
    }

    @Test
    @DisplayName("[창고 수정] 성공 테스트")
    void testUpdateWarehouseSuccess() throws Exception {
        // given
        when(warehouseCommandService.updateWarehouse(eq(1L), any())).thenReturn(warehouseResponse);

        // when & then
        mockMvc.perform(put("/api/v1/warehouse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.warehouseId").value(1L))
                .andExpect(jsonPath("$.data.warehouseName").value("Updated Name"));
    }

    @Test
    @DisplayName("[창고 수정] 창고 없음 예외 테스트")
    void testUpdateWarehouse_NotFound() throws Exception {
        // given
        WarehouseUpdateRequest request = WarehouseUpdateRequest.builder()
                .warehouseName("New")
                .warehouseAddress(addressRequest)
                .warehouseStatus(false)
                .build();

        when(warehouseCommandService.updateWarehouse(eq(1L), any()))
                .thenThrow(new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // when & then
        mockMvc.perform(put("/api/v1/warehouse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(WarehouseErrorCode.WAREHOUSE_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[창고 삭제] 성공 테스트")
    void testDeleteWarehouseSuccess() throws Exception {
        // given
        when(warehouseCommandService.deleteWarehouse(1L)).thenReturn(warehouseResponse);

        // when & then
        mockMvc.perform(delete("/api/v1/warehouse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.warehouseId").value(1L));
    }

    @Test
    @DisplayName("[창고 삭제] 창고 없음 예외 테스트")
    void testDeleteWarehouse_NotFound() throws Exception {
        // given
        when(warehouseCommandService.deleteWarehouse(eq(1L)))
                .thenThrow(new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // when & then
        mockMvc.perform(delete("/api/v1/warehouse/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(WarehouseErrorCode.WAREHOUSE_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[창고 삭제] 활성 상태 창고 삭제 불가 예외 테스트")
    void testDeleteWarehouse_InvalidStatus() throws Exception {
        // given
        when(warehouseCommandService.deleteWarehouse(eq(1L)))
                .thenThrow(new WarehouseException(WarehouseErrorCode.INVALID_WAREHOUSE_STATUS));

        // when & then
        mockMvc.perform(delete("/api/v1/warehouse/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(WarehouseErrorCode.INVALID_WAREHOUSE_STATUS.getErrorCode()));
    }

    @Test
    @DisplayName("[보유 재고 등록] 성공 테스트")
    @WithMockCustomUser(memberId = 200L, position = "WAREHOUSE_MANAGER")
    void testRegisterInventorySuccess() throws Exception {
        // given
        WarehouseInventoryCreateRequest request = WarehouseInventoryCreateRequest.builder()
                .items(List.of(WarehouseInventoryCreateRequest.InventoryItem.builder()
                        .productId(1L)
                        .quantity(100)
                        .build()))
                .build();

        when(warehouseCommandService.registerInventory(eq(1L), any(), eq(200L)))
                .thenReturn(warehouseResponse);

        // when & then
        mockMvc.perform(post("/api/v1/warehouse/1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.warehouseId").value(1L));
    }

    @Test
    @DisplayName("[보유 재고 등록] 창고 없음 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "WAREHOUSE_MANAGER")
    void testRegisterInventory_WarehouseNotFound() throws Exception {
        // given
        WarehouseInventoryCreateRequest request = WarehouseInventoryCreateRequest.builder()
                .items(List.of(
                        WarehouseInventoryCreateRequest.InventoryItem.builder()
                                .productId(1L)
                                .quantity(100)
                                .build()
                ))
                .build();

        when(warehouseCommandService.registerInventory(eq(1L), any(), eq(200L)))
                .thenThrow(new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/api/v1/warehouse/1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(WarehouseErrorCode.WAREHOUSE_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[보유 재고 수정] 성공 테스트")
    @WithMockCustomUser(memberId = 200L, position = "WAREHOUSE_MANAGER")
    void testUpdateInventorySuccess() throws Exception {
        // given
        WarehouseInventoryUpdateRequest request = WarehouseInventoryUpdateRequest.builder()
                .quantity(200)
                .build();

        when(warehouseCommandService.updateInventory(eq(1L), any(), eq(200L)))
                .thenReturn(inventoryResponse);

        // when & then
        mockMvc.perform(put("/api/v1/warehouse/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inventoryId").value(1L))
                .andExpect(jsonPath("$.data.quantity").value(100));
    }

    @Test
    @DisplayName("[보유 재고 수정] 재고 없음 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "WAREHOUSE_MANAGER")
    void testUpdateInventory_NotFound() throws Exception {
        // given
        WarehouseInventoryUpdateRequest request = WarehouseInventoryUpdateRequest.builder()
                .quantity(100)
                .build();

        when(warehouseCommandService.updateInventory(eq(1L), any(), eq(200L)))
                .thenThrow(new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND));

        // when & then
        mockMvc.perform(put("/api/v1/warehouse/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(WarehouseErrorCode.INVENTORY_NOT_FOUND.getErrorCode()));
    }

    @Test
    @DisplayName("[보유 재고 삭제] 성공 테스트")
    @WithMockCustomUser(memberId = 200L, position = "WAREHOUSE_MANAGER")
    void testDeleteInventorySuccess() throws Exception {
        // given
        when(warehouseCommandService.deleteInventory(eq(1L), eq(200L)))
                .thenReturn(inventoryResponse);

        // when & then
        mockMvc.perform(delete("/api/v1/warehouse/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inventoryId").value(1L));
    }

    @Test
    @DisplayName("[보유 재고 삭제] 재고 없음 예외 테스트")
    @WithMockCustomUser(memberId = 200L, position = "WAREHOUSE_MANAGER")
    void testDeleteInventory_NotFound() throws Exception {
        // given
        when(warehouseCommandService.deleteInventory(eq(1L), eq(200L)))
                .thenThrow(new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND));

        // when & then
        mockMvc.perform(delete("/api/v1/warehouse/inventory/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(WarehouseErrorCode.INVENTORY_NOT_FOUND.getErrorCode()));
    }

}

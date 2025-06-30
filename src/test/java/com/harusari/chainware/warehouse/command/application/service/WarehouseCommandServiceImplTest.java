package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("[창고 - service] WarehouseCommandServiceImpl 테스트")
class WarehouseCommandServiceImplTest {

    @InjectMocks
    private WarehouseCommandServiceImpl warehouseCommandService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseInventoryRepository warehouseInventoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[창고 수정] 수정 성공 테스트")
    void testUpdateWarehouseSuccess() {
        // given
        Warehouse warehouse = Warehouse.builder()
                .warehouseName("Old")
                .warehouseAddress("Old Address")
                .warehouseStatus(true)
                .build();
        ReflectionTestUtils.setField(warehouse, "warehouseId", 1L);

        WarehouseUpdateRequest request = WarehouseUpdateRequest.builder()
                .warehouseName("New")
                .warehouseAddress("New Address")
                .warehouseStatus(false)
                .build();

        given(warehouseRepository.findById(1L)).willReturn(Optional.of(warehouse));

        // when
        WarehouseCommandResponse response = warehouseCommandService.updateWarehouse(1L, request);

        // then
        assertThat(response.getWarehouseName()).isEqualTo("New");
        assertThat(response.isWarehouseStatus()).isFalse();
    }

    @Test
    @DisplayName("[창고 수정] 창고 없음 예외 테스트")
    void testUpdateWarehouseNotFound() {
        // given
        given(warehouseRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> warehouseCommandService.updateWarehouse(1L,
                WarehouseUpdateRequest.builder()
                        .warehouseName("창고 이름")
                        .warehouseAddress("창고 주소")
                        .warehouseStatus(true).build()))
                .isInstanceOf(WarehouseException.class)
                .hasFieldOrPropertyWithValue("errorCode", WarehouseErrorCode.WAREHOUSE_NOT_FOUND);
    }

    @Test
    @DisplayName("[창고 삭제] 삭제 성공 테스트")
    void testDeleteWarehouseSuccess() {
        // given
        Warehouse warehouse = Warehouse.builder()
                .warehouseStatus(false)
                .build();
        ReflectionTestUtils.setField(warehouse, "warehouseId", 1L);
        given(warehouseRepository.findById(1L)).willReturn(Optional.of(warehouse));

        // when
        WarehouseCommandResponse response = warehouseCommandService.deleteWarehouse(1L);

        // then
        assertThat(response.getWarehouseId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("[창고 삭제] 창고 없음 예외 테스트")
    void testDeleteWarehouseNotFound() {
        // given
        given(warehouseRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> warehouseCommandService.deleteWarehouse(1L))
                .isInstanceOf(WarehouseException.class)
                .hasFieldOrPropertyWithValue("errorCode", WarehouseErrorCode.WAREHOUSE_NOT_FOUND);
    }

    @Test
    @DisplayName("[창고 삭제] 활성 상태 창고 삭제 불가 테스트")
    void testDeleteWarehouseInvalidStatus() {
        // given
        Warehouse warehouse = Warehouse.builder()
                .warehouseStatus(true)
                .build();
        given(warehouseRepository.findById(1L)).willReturn(Optional.of(warehouse));

        // when & then
        assertThatThrownBy(() -> warehouseCommandService.deleteWarehouse(1L))
                .isInstanceOf(WarehouseException.class)
                .hasFieldOrPropertyWithValue("errorCode", WarehouseErrorCode.INVALID_WAREHOUSE_STATUS);
    }

    @Test
    @DisplayName("[보유 재고 등록] 등록 성공 테스트")
    void testRegisterInventorySuccess() {
        // given
        Warehouse warehouse = Warehouse.builder().build();
        ReflectionTestUtils.setField(warehouse, "warehouseId", 1L);

        WarehouseInventoryCreateRequest request = WarehouseInventoryCreateRequest.builder()
                .items(List.of(
                        WarehouseInventoryCreateRequest.InventoryItem.builder()
                                .productId(1L)
                                .quantity(100)
                                .build()
                ))
                .build();

        given(warehouseRepository.findById(1L)).willReturn(Optional.of(warehouse));

        // when
        WarehouseCommandResponse response = warehouseCommandService.registerInventory(1L, request);

        // then
        assertThat(response.getWarehouseId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("[보유 재고 등록] 창고 없음 예외 테스트")
    void testRegisterInventoryWarehouseNotFound() {
        // given
        WarehouseInventoryCreateRequest request = WarehouseInventoryCreateRequest.builder()
                .items(List.of(
                        WarehouseInventoryCreateRequest.InventoryItem.builder()
                                .productId(1L)
                                .quantity(100)
                                .build()
                ))
                .build();

        given(warehouseRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> warehouseCommandService.registerInventory(1L, request))
                .isInstanceOf(WarehouseException.class)
                .hasFieldOrPropertyWithValue("errorCode", WarehouseErrorCode.WAREHOUSE_NOT_FOUND);
    }

    @Test
    @DisplayName("[재고 수정] 수정 성공 테스트")
    void testUpdateInventorySuccess() {
        // given
        WarehouseInventory inventory = WarehouseInventory.builder()
                .quantity(50)
                .build();
        ReflectionTestUtils.setField(inventory, "warehouseInventoryId", 1L);

        given(warehouseInventoryRepository.findById(1L)).willReturn(Optional.of(inventory));

        // when
        WarehouseInventoryCommandResponse response = warehouseCommandService.updateInventory(
                1L, WarehouseInventoryUpdateRequest.builder().quantity(150).build());

        // then
        assertThat(response.getQuantity()).isEqualTo(150);
    }

    @Test
    @DisplayName("[재고 수정] 재고 없음 예외 테스트")
    void testUpdateInventoryNotFound() {
        // given
        given(warehouseInventoryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> warehouseCommandService.updateInventory(
                1L, WarehouseInventoryUpdateRequest.builder().quantity(100).build()))
                .isInstanceOf(WarehouseException.class)
                .hasFieldOrPropertyWithValue("errorCode", WarehouseErrorCode.INVENTORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[재고 삭제] 삭제 성공 테스트")
    void testDeleteInventorySuccess() {
        // given
        WarehouseInventory inventory = WarehouseInventory.builder()
                .quantity(50)
                .build();
        ReflectionTestUtils.setField(inventory, "warehouseInventoryId", 1L);

        given(warehouseInventoryRepository.findById(1L)).willReturn(Optional.of(inventory));

        // when
        WarehouseInventoryCommandResponse response = warehouseCommandService.deleteInventory(1L);

        // then
        assertThat(response.getInventoryId()).isEqualTo(1L);
        verify(warehouseInventoryRepository).delete(inventory);
    }

    @Test
    @DisplayName("[재고 삭제] 재고 없음 예외 테스트")
    void testDeleteInventoryNotFound() {
        // given
        given(warehouseInventoryRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> warehouseCommandService.deleteInventory(1L))
                .isInstanceOf(WarehouseException.class)
                .hasFieldOrPropertyWithValue("errorCode", WarehouseErrorCode.INVENTORY_NOT_FOUND);
    }


}

package com.harusari.chainware.delivery.command.application.service;

import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartItem;
import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.delivery.exception.DeliveryErrorCode;
import com.harusari.chainware.delivery.exception.DeliveryException;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseOutboundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("[배송 - service] DeliveryCommandServiceImpl 테스트")
class DeliveryCommandServiceImplTest {

    @InjectMocks
    private DeliveryCommandServiceImpl deliveryCommandService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private WarehouseInventoryRepository warehouseInventoryRepository;

    @Mock
    private WarehouseOutboundRepository warehouseOutboundRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[배송 시작] 성공 테스트")
    void testStartDeliverySuccess() {
        // given
        Long deliveryId = 1L;
        Long orderId = 100L;

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .warehouseId(10L)
                .build();
        ReflectionTestUtils.setField(delivery, "deliveryId", deliveryId);

        List<OrderDetail> details = List.of(
                OrderDetail.builder().productId(1L).quantity(3).build()
        );

        WarehouseInventory inventory = WarehouseInventory.builder()
                .productId(1L)
                .warehouseId(10L)
                .quantity(10)
                .reservedQuantity(10)
                .build();

        DeliveryStartItem item = DeliveryStartItem.builder()
                .productId(1L)
                .quantity(3)
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .products(List.of(item))
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(orderDetailRepository.findByOrderId(orderId)).willReturn(details);
        given(warehouseInventoryRepository.findByWarehouseIdAndProductIdForUpdate(10L, 1L)).willReturn(Optional.of(inventory));

        // when
        DeliveryCommandResponse response = deliveryCommandService.startDelivery(deliveryId, request);

        // then
        assertThat(response.getDeliveryId()).isEqualTo(deliveryId);
        assertThat(response.getDeliveryStatus()).isEqualTo(DeliveryStatus.IN_TRANSIT);
    }

    @Test
    @DisplayName("[배송 시작] 존재하지 않는 배송 ID 예외 테스트")
    void testStartDelivery_DeliveryNotFound() {
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryCommandService.startDelivery(1L, mock(DeliveryStartRequest.class)))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_NOT_FOUND);
    }

    @Test
    @DisplayName("[배송 시작] 요청 상태 아님 예외 테스트")
    void testStartDelivery_InvalidStatus() {
        Delivery delivery = Delivery.builder()
                .orderId(1L)
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .build();
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.of(delivery));

        assertThatThrownBy(() -> deliveryCommandService.startDelivery(1L, mock(DeliveryStartRequest.class)))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_STATUS_NOT_REQUESTED);
    }

    @Test
    @DisplayName("[배송 시작] 주문 상세 없음 예외 테스트")
    void testStartDelivery_NoOrderDetails() {
        Delivery delivery = Delivery.builder()
                .orderId(1L)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.of(delivery));
        given(orderDetailRepository.findByOrderId(anyLong())).willReturn(List.of());

        assertThatThrownBy(() -> deliveryCommandService.startDelivery(1L, mock(DeliveryStartRequest.class)))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.ORDER_DETAIL_NOT_FOUND_FOR_DELIVERY);
    }

    @Test
    @DisplayName("[배송 시작] 재고 부족 예외 테스트")
    void testStartDelivery_InsufficientInventory() {
        //given
        Long deliveryId = 1L;

        Delivery delivery = Delivery.builder()
                .orderId(1L)
                .warehouseId(10L)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();
        List<OrderDetail> details = List.of(OrderDetail.builder().productId(1L).quantity(5).build());

        WarehouseInventory inventory = WarehouseInventory.builder()
                .productId(1L)
                .warehouseId(10L)
                .quantity(3)  // 부족한 재고
                .reservedQuantity(3)
                .build();

        DeliveryStartItem item = DeliveryStartItem.builder()
                .productId(1L)
                .quantity(5)
                .expirationDate(LocalDate.now())
                .build();

        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .products(List.of(item))
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(orderDetailRepository.findByOrderId(1L)).willReturn(details);
        given(warehouseInventoryRepository.findByWarehouseIdAndProductIdForUpdate(10L, 1L)).willReturn(Optional.of(inventory));

        assertThatThrownBy(() -> deliveryCommandService.startDelivery(deliveryId, request))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.INSUFFICIENT_INVENTORY_FOR_DELIVERY);
    }

    @Test
    @DisplayName("[배송 완료] 성공 테스트")
    void testCompleteDeliverySuccess() {
        Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .build();
        ReflectionTestUtils.setField(delivery, "deliveryId", 1L);

        given(deliveryRepository.findById(1L)).willReturn(Optional.of(delivery));

        DeliveryCommandResponse response = deliveryCommandService.completeDelivery(1L);

        assertThat(response.getDeliveryId()).isEqualTo(1L);
        assertThat(response.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED);
    }

    @Test
    @DisplayName("[배송 완료] 배송 정보 없음 예외 테스트")
    void testCompleteDelivery_DeliveryNotFound() {
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryCommandService.completeDelivery(1L))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_NOT_FOUND);
    }

    @Test
    @DisplayName("[배송 완료] 상태가 '배송 중'이 아님 예외 테스트")
    void testCompleteDelivery_InvalidStatus() {
        Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.of(delivery));

        assertThatThrownBy(() -> deliveryCommandService.completeDelivery(1L))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_STATUS_NOT_IN_TRANSIT);
    }
}

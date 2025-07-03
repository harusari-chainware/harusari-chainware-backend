package com.harusari.chainware.delivery.command.application.service;

import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartItem;
import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.delivery.exception.DeliveryErrorCode;
import com.harusari.chainware.delivery.exception.DeliveryException;
import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseOutboundRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
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
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private WarehouseInventoryRepository warehouseInventoryRepository;

    @Mock
    private WarehouseOutboundRepository warehouseOutboundRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

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
        Long orderDetailId = 10L;
        Long warehouseId = 10L;
        Long memberId = 999L;

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .warehouseId(warehouseId)
                .build();
        ReflectionTestUtils.setField(delivery, "deliveryId", deliveryId);

        OrderDetail orderDetail = OrderDetail.builder()
                .productId(1L)
                .quantity(3)
                .build();
        ReflectionTestUtils.setField(orderDetail, "orderDetailId", orderDetailId);
        ReflectionTestUtils.setField(orderDetail, "orderId", orderId);

        WarehouseInventory inventory = WarehouseInventory.builder()
                .productId(1L)
                .warehouseId(warehouseId)
                .quantity(10)
                .reservedQuantity(10)
                .build();

        DeliveryStartItem item = DeliveryStartItem.builder()
                .orderDetailId(orderDetailId)
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .products(List.of(item))
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(orderDetail));
        given(warehouseInventoryRepository.findByWarehouseIdAndProductIdForUpdate(warehouseId, 1L)).willReturn(Optional.of(inventory));
        given(warehouseRepository.findById(warehouseId)).willReturn(
                Optional.of(Warehouse.builder().memberId(memberId).build())
        );

        // when
        DeliveryCommandResponse response = deliveryCommandService.startDelivery(deliveryId, request, memberId);

        // then
        assertThat(response.getDeliveryId()).isEqualTo(deliveryId);
        assertThat(response.getDeliveryStatus()).isEqualTo(DeliveryStatus.IN_TRANSIT);
    }


    @Test
    @DisplayName("[배송 시작] 창고 관리자 아님 예외 테스트")
    void testStartDelivery_UnauthorizedWarehouseManager() {
        // given
        Long deliveryId = 1L;
        Long memberId = 123L; // 로그인 사용자
        Long wrongManagerId = 999L; // 창고 담당자

        Delivery delivery = Delivery.builder()
                .warehouseId(10L)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();
        ReflectionTestUtils.setField(delivery, "deliveryId", deliveryId);

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(warehouseRepository.findById(10L)).willReturn(Optional.of(
                Warehouse.builder().memberId(wrongManagerId).build()
        ));

        // when & then
        assertThatThrownBy(() -> deliveryCommandService.startDelivery(deliveryId, DeliveryStartRequest.builder().build(), memberId))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.UNAUTHORIZED_WAREHOUSE_MANAGER);
    }

    @Test
    @DisplayName("[배송 시작] 존재하지 않는 배송 ID 예외 테스트")
    void testStartDelivery_DeliveryNotFound() {
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryCommandService.startDelivery(1L, mock(DeliveryStartRequest.class), 100L))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_NOT_FOUND);
    }

    @Test
    @DisplayName("[배송 시작] 요청 상태 아님 예외 테스트")
    void testStartDelivery_InvalidStatus() {
        // given
        Delivery delivery = Delivery.builder()
                .orderId(1L)
                .warehouseId(10L)
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .build();
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.of(delivery));

        // 창고 관리자 권한 확인을 위한 warehouseRepository mocking 필요
        given(warehouseRepository.findById(10L))
                .willReturn(Optional.of(Warehouse.builder().memberId(100L).build())); // 권한 정상

        // when & then
        assertThatThrownBy(() -> deliveryCommandService.startDelivery(1L, mock(DeliveryStartRequest.class), 100L))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_STATUS_NOT_REQUESTED);
    }


    @Test
    @DisplayName("[배송 시작] 주문 상세 없음 예외 테스트")
    void testStartDelivery_NoOrderDetails() {
        // given
        Long deliveryId = 1L;
        Long orderDetailId = 999L;
        Long warehouseId = 10L;
        Long memberId = 100L;

        Delivery delivery = Delivery.builder()
                .orderId(1L)
                .warehouseId(warehouseId)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();

        DeliveryStartItem item = DeliveryStartItem.builder()
                .orderDetailId(orderDetailId)
                .expirationDate(LocalDate.of(2025, 12, 31))
                .build();

        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .products(List.of(item))
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(warehouseRepository.findById(warehouseId))
                .willReturn(Optional.of(Warehouse.builder().memberId(memberId).build())); // 권한 정상
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> deliveryCommandService.startDelivery(deliveryId, request, memberId))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.ORDER_DETAIL_NOT_FOUND_FOR_DELIVERY);
    }


    @Test
    @DisplayName("[배송 시작] 재고 부족 예외 테스트")
    void testStartDelivery_InsufficientInventory() {
        // given
        Long deliveryId = 1L;
        Long orderDetailId = 10L;
        Long warehouseId = 10L;
        Long memberId = 100L;
        Long orderId = 1L;

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .warehouseId(warehouseId)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();

        OrderDetail detail = OrderDetail.builder()
                .productId(1L)
                .quantity(5)
                .build();
        ReflectionTestUtils.setField(detail, "orderDetailId", orderDetailId);
        ReflectionTestUtils.setField(detail, "orderId", orderId); // ✅ 추가

        WarehouseInventory inventory = WarehouseInventory.builder()
                .productId(1L)
                .warehouseId(warehouseId)
                .quantity(3)  // 부족
                .reservedQuantity(3)
                .build();

        DeliveryStartItem item = DeliveryStartItem.builder()
                .orderDetailId(orderDetailId)
                .expirationDate(LocalDate.now())
                .build();

        DeliveryStartRequest request = DeliveryStartRequest.builder()
                .carrier("CJ대한통운")
                .products(List.of(item))
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(warehouseRepository.findById(warehouseId))
                .willReturn(Optional.of(Warehouse.builder().memberId(memberId).build()));
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(detail));
        given(warehouseInventoryRepository.findByWarehouseIdAndProductIdForUpdate(warehouseId, 1L))
                .willReturn(Optional.of(inventory));

        // when & then
        assertThatThrownBy(() -> deliveryCommandService.startDelivery(deliveryId, request, memberId))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.INSUFFICIENT_INVENTORY_FOR_DELIVERY);
    }


    @Test
    @DisplayName("[배송 완료] 성공 테스트")
    void testCompleteDeliverySuccess() {
        // given
        Long deliveryId = 1L;
        Long orderId = 10L;
        Long memberId = 100L;

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .build();
        ReflectionTestUtils.setField(delivery, "deliveryId", deliveryId);

        Order order = Order.builder()
                .memberId(memberId)
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        DeliveryCommandResponse response = deliveryCommandService.completeDelivery(deliveryId, memberId);

        // then
        assertThat(response.getDeliveryId()).isEqualTo(deliveryId);
        assertThat(response.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED);
    }

    @Test
    @DisplayName("[배송 완료] 배송 정보 없음 예외 테스트")
    void testCompleteDelivery_DeliveryNotFound() {
        // given
        given(deliveryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> deliveryCommandService.completeDelivery(1L, 100L))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_NOT_FOUND);
    }

    @Test
    @DisplayName("[배송 완료] 상태가 '배송 중'이 아님 예외 테스트")
    void testCompleteDelivery_InvalidStatus() {
        // given
        Long deliveryId = 1L;
        Long orderId = 10L;
        Long memberId = 100L;

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .build();
        ReflectionTestUtils.setField(delivery, "deliveryId", deliveryId);

        Order order = Order.builder()
                .memberId(memberId)
                .build();

        given(deliveryRepository.findById(deliveryId)).willReturn(Optional.of(delivery));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> deliveryCommandService.completeDelivery(deliveryId, memberId))
                .isInstanceOf(DeliveryException.class)
                .hasFieldOrPropertyWithValue("errorCode", DeliveryErrorCode.DELIVERY_STATUS_NOT_IN_TRANSIT);
    }

}

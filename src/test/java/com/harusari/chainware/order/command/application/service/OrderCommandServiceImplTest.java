package com.harusari.chainware.order.command.application.service;

import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.order.command.application.dto.request.*;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderException;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("[주문 - service] OrderCommandServiceImpl 테스트")
class OrderCommandServiceImplTest {

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private WarehouseInventoryRepository warehouseInventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    private OrderCreateRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
        given(redissonClient.getLock(anyString())).willReturn(rLock);

        validRequest = OrderCreateRequest.builder()
                .deliveryDueDate(LocalDate.now().plusDays(3))
                .orderDetails(List.of(
                        OrderDetailCreateRequest.builder()
                                .productId(1L)
                                .quantity(3)
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("[주문 등록] 성공 테스트")
    void testCreateOrderSuccess() {
        // given
        WarehouseInventory inventory = WarehouseInventory.builder()
                .productId(1L)
                .quantity(100)
                .reservedQuantity(10)
                .build();

        Product product = Product.builder()
                .productId(1L)
                .basePrice(1500)
                .build();

        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        Franchise franchise = Franchise.builder()
                .memberId(100L)
                .build();
        ReflectionTestUtils.setField(franchise, "franchiseId", 1L);

        given(franchiseRepository.findFranchiseIdByMemberId(100L))
                .willReturn(Optional.of(franchise));

        Order savedOrder = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(validRequest.getDeliveryDueDate())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        ReflectionTestUtils.setField(savedOrder, "orderId", 1L);

        given(warehouseInventoryRepository.findByProductIdForUpdate(1L)).willReturn(Optional.of(inventory));
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

        // when
        OrderCommandResponse response = orderCommandService.createOrder(validRequest, 100L);

        // then
        assertThat(response.getOrderId()).isEqualTo(1L);
        assertThat(response.getFranchiseId()).isEqualTo(1L);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.REQUESTED);

        verify(orderRepository).save(any());
        verify(orderDetailRepository).saveAll(any());
    }

    @Test
    @DisplayName("[주문 등록] 빈 주문 상세 예외 테스트")
    void testCreateOrderEmptyDetails() throws Exception {
        // given
        OrderCreateRequest emptyRequest = OrderCreateRequest.builder()
                .deliveryDueDate(LocalDate.now())
                .orderDetails(List.of())
                .build();

        given(rLock.tryLock(anyLong(), anyLong(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> orderCommandService.createOrder(emptyRequest, 100L))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.EMPTY_ORDER_DETAIL);
    }

    @Test
    @DisplayName("[주문 수정] 성공 테스트")
    void testUpdateOrderSuccess() {
        // given
        Long orderId = 1L;
        Long memberId = 100L;

        OrderUpdateRequest updateRequest = OrderUpdateRequest.builder()
                .deliveryDueDate(LocalDate.of(2025, 6, 28))
                .orderDetails(List.of(
                        OrderDetailUpdateRequest.builder().productId(1L).quantity(3).build(),
                        OrderDetailUpdateRequest.builder().productId(2L).quantity(2).build()
                ))
                .build();

        Order existingOrder = Order.builder()
                .franchiseId(1L)
                .memberId(memberId)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(1)
                .totalPrice(1000L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(existingOrder, "orderId", orderId);

        WarehouseInventory inventory1 = WarehouseInventory.builder().productId(1L).quantity(100).reservedQuantity(10).build();
        WarehouseInventory inventory2 = WarehouseInventory.builder().productId(2L).quantity(100).reservedQuantity(5).build();

        Product product1 = Product.builder().productId(1L).basePrice(1500).build();
        Product product2 = Product.builder().productId(2L).basePrice(2000).build();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(existingOrder));
        given(warehouseInventoryRepository.findByProductIdForUpdate(1L)).willReturn(Optional.of(inventory1));
        given(warehouseInventoryRepository.findByProductIdForUpdate(2L)).willReturn(Optional.of(inventory2));
        given(productRepository.findById(1L)).willReturn(Optional.of(product1));
        given(productRepository.findById(2L)).willReturn(Optional.of(product2));

        // when
        OrderCommandResponse response = orderCommandService.updateOrder(orderId, updateRequest, memberId);

        // then
        assertThat(response.getOrderId()).isEqualTo(orderId);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.REQUESTED);
        verify(orderDetailRepository).deleteAllByOrderId(orderId);
        verify(orderDetailRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("[주문 수정] 권한 불일치 예외 테스트")
    void testUpdateOrder_InvalidOwner() {
        // given
        Long orderId = 1L;
        Long memberId = 999L; // 다른 유저

        OrderUpdateRequest updateRequest = OrderUpdateRequest.builder()
                .deliveryDueDate(LocalDate.of(2025, 6, 28))
                .orderDetails(List.of(
                        OrderDetailUpdateRequest.builder().productId(1L).quantity(3).build()
                ))
                .build();

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(1)
                .totalPrice(1000L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderCommandService.updateOrder(orderId, updateRequest, memberId))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.UNAUTHORIZED_ORDER_ACCESS);
    }


    @Test
    @DisplayName("[주문 취소] 성공 테스트")
    void testCancelOrderSuccess() {
        // given
        Long orderId = 1L;
        Long memberId = 100L;

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(memberId)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(2)
                .totalQuantity(5)
                .totalPrice(7500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        OrderCommandResponse response = orderCommandService.cancelOrder(orderId, memberId);

        // then
        assertThat(response.getOrderId()).isEqualTo(orderId);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("[주문 취소] 주문자 본인이 아닐 때 예외 발생")
    void testCancelOrder_NotOwner() {
        // given
        Long orderId = 1L;
        Long otherMemberId = 999L;

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(2)
                .totalQuantity(5)
                .totalPrice(7500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderCommandService.cancelOrder(orderId, otherMemberId))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.UNAUTHORIZED_ORDER_ACCESS);
    }

    @Test
    @DisplayName("[주문 취소] 이미 취소된 주문에 대해 예외 발생")
    void testCancelOrder_AlreadyCancelled() {
        // given
        Long orderId = 1L;
        Long memberId = 100L;

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(memberId)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(2)
                .totalQuantity(5)
                .totalPrice(7500L)
                .orderStatus(OrderStatus.CANCELLED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderCommandService.cancelOrder(orderId, memberId))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.CANNOT_CANCEL_ORDER);
    }

    @Test
    @DisplayName("[주문 승인] 성공 테스트")
    void testApproveOrderSuccess() throws Exception {
        // given
        Long orderId = 1L;
        Long approverId = 200L;
        Long warehouseId = 10L;

        OrderApproveRequest request = OrderApproveRequest.builder()
                .warehouseId(warehouseId)
                .build();

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        OrderDetail detail = OrderDetail.builder()
                .orderId(orderId)
                .productId(1L)
                .quantity(3)
                .unitPrice(1500)
                .totalPrice(4500L)
                .createdAt(LocalDateTime.now())
                .build();

        WarehouseInventory inventory = WarehouseInventory.builder()
                .warehouseId(warehouseId)
                .productId(1L)
                .quantity(100)
                .reservedQuantity(10)
                .build();

        RLock mockLock = mock(RLock.class);
        given(redissonClient.getLock(any())).willReturn(mockLock);

        given(mockLock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderDetailRepository.findByOrderId(orderId)).willReturn(List.of(detail));
        given(warehouseInventoryRepository.findByWarehouseIdAndProductIdForUpdate(warehouseId, 1L)).willReturn(Optional.of(inventory));
        given(deliveryRepository.save(any())).willReturn(mock(Delivery.class));

        // when
        OrderCommandResponse response = orderCommandService.approveOrder(orderId, request, approverId);

        // then
        assertThat(response.getOrderId()).isEqualTo(orderId);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.APPROVED);

        verify(deliveryRepository).save(any(Delivery.class));
        verify(warehouseInventoryRepository).findByWarehouseIdAndProductIdForUpdate(warehouseId, 1L);
    }

    @Test
    @DisplayName("[주문 승인] 주문 상태가 REQUESTED가 아닌 경우 예외 발생")
    void testApproveOrder_InvalidStatus() throws Exception{
        // given
        Long orderId = 1L;
        Long warehouseId = 10L;
        Long approverId = 200L;

        OrderApproveRequest request = OrderApproveRequest.builder()
                .warehouseId(warehouseId)
                .build();

        Order alreadyApproved = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.APPROVED) // 이미 승인됨
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(alreadyApproved, "orderId", orderId);

        RLock mockLock = mock(RLock.class);
        given(redissonClient.getLock(any())).willReturn(mockLock);
        given(mockLock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(alreadyApproved));

        // when & then
        assertThatThrownBy(() -> orderCommandService.approveOrder(orderId, request, approverId))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.CANNOT_APPROVE_ORDER);
    }

    @Test
    @DisplayName("[주문 승인] 재고 없음 예외 발생")
    void testApproveOrder_NoInventory() throws Exception {
        // given
        Long orderId = 1L;
        Long warehouseId = 10L;
        Long approverId = 200L;

        OrderApproveRequest request = OrderApproveRequest.builder()
                .warehouseId(warehouseId)
                .build();

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        OrderDetail detail = OrderDetail.builder()
                .orderId(orderId)
                .productId(1L)
                .quantity(3)
                .unitPrice(1500)
                .totalPrice(4500L)
                .createdAt(LocalDateTime.now())
                .build();
        RLock mockLock = mock(RLock.class);
        given(redissonClient.getLock(any())).willReturn(mockLock);

        given(mockLock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderDetailRepository.findByOrderId(orderId)).willReturn(List.of(detail));
        given(warehouseInventoryRepository.findByWarehouseIdAndProductIdForUpdate(warehouseId, 1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderCommandService.approveOrder(orderId, request, approverId))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.PRODUCT_INVENTORY_NOT_FOUND);
    }


    @Test
    @DisplayName("[주문 반려] 성공 테스트")
    void testRejectOrderSuccess() {
        // given
        Long orderId = 1L;
        Long memberId = 200L;

        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        OrderRejectRequest rejectRequest = OrderRejectRequest.builder()
                .rejectReason("재고 부족으로 반려합니다.")
                .build();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        OrderCommandResponse response = orderCommandService.rejectOrder(orderId, rejectRequest, memberId);

        // then
        assertThat(response.getOrderId()).isEqualTo(orderId);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.REJECTED);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.REJECTED);
        assertThat(order.getRejectReason()).isEqualTo(rejectRequest.getRejectReason());
    }

    @Test
    @DisplayName("[주문 반려] 주문 상태가 REQUESTED가 아닐 경우 예외 발생")
    void testRejectOrder_InvalidStatus() {
        // given
        Long orderId = 1L;
        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.APPROVED) // 이미 승인됨
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        OrderRejectRequest rejectRequest = OrderRejectRequest.builder()
                .rejectReason("사유")
                .build();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderCommandService.rejectOrder(orderId, rejectRequest, 200L))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.CANNOT_REJECT_ORDER);
    }

    @Test
    @DisplayName("[주문 반려] 반려 사유 누락 시 예외 발생")
    void testRejectOrder_BlankReason() {
        // given
        Long orderId = 1L;
        Order order = Order.builder()
                .franchiseId(1L)
                .memberId(100L)
                .orderCode("SO-240623001")
                .deliveryDueDate(LocalDate.now())
                .productCount(1)
                .totalQuantity(3)
                .totalPrice(4500L)
                .orderStatus(OrderStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(order, "orderId", orderId);

        OrderRejectRequest rejectRequest = OrderRejectRequest.builder()
                .rejectReason(" ") // 공백 문자열
                .build();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderCommandService.rejectOrder(orderId, rejectRequest, 200L))
                .isInstanceOf(OrderException.class)
                .hasFieldOrPropertyWithValue("errorCode", OrderErrorCode.REJECT_REASON_REQUIRED);
    }


}

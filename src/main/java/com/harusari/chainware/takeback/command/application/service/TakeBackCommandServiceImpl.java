package com.harusari.chainware.takeback.command.application.service;

import com.harusari.chainware.common.infrastructure.storage.StorageUploader;
import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryMethod;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.delivery.exception.DeliveryErrorCode;
import com.harusari.chainware.delivery.exception.DeliveryException;
import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderException;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.request.TakeBackRejectRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBack;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackDetail;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import com.harusari.chainware.takeback.command.domain.repository.TakeBackDetailRepository;
import com.harusari.chainware.takeback.command.domain.repository.TakeBackRepository;
import com.harusari.chainware.takeback.exception.TakeBackErrorCode;
import com.harusari.chainware.takeback.exception.TakeBackException;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class TakeBackCommandServiceImpl implements TakeBackCommandService {

    private final TakeBackRepository takeBackRepository;
    private final TakeBackDetailRepository takeBackDetailRepository;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DeliveryRepository deliveryRepository;
    private final WarehouseRepository warehouseRepository;
    private final StorageUploader storageUploader;

    private static final String TAKE_BACK_IMAGE_DIR = "takeback";

    // 반품 신청
    @Override
    public TakeBackCommandResponse createTakeBack(TakeBackCreateRequest request, List<MultipartFile> imageFiles, Long memberId) {
        // 0. 유효성 검증
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.getMemberId().equals(memberId)) {
            throw new TakeBackException(TakeBackErrorCode.UNAUTHORIZED_ACCESS_TO_ORDER);
        }

        List<TakeBackCreateRequest.TakeBackItemRequest> items = request.getItems();

        if (items == null || items.isEmpty()) {
            throw new TakeBackException(TakeBackErrorCode.INVALID_TAKE_BACK_ITEMS);
        }

        if (imageFiles == null || imageFiles.size() != items.size()) {
            throw new TakeBackException(TakeBackErrorCode.IMAGE_COUNT_MISMATCH);
        }

        // 1. 반품 엔티티 저장
        String takeBackCode = generateTakeBackCode();
        TakeBack takeBack = takeBackRepository.save(TakeBack.builder()
                .orderId(request.getOrderId())
                .takeBackCode(takeBackCode)
                .build());

        // 2. 반품 상세 저장 (반품 이미지 포함)
        List<TakeBackDetail> details = IntStream.range(0, items.size())
                .mapToObj(i -> {
                    TakeBackCreateRequest.TakeBackItemRequest item = items.get(i);

                    // 반품 이미지 불러오기
                    MultipartFile imageFile = imageFiles.get(i);

                    if (imageFile == null || imageFile.isEmpty()) {
                        throw new TakeBackException(TakeBackErrorCode.IMAGE_FILE_NOT_FOUND);
                    }

                    String filePath = storageUploader.uploadTakeBackImage(imageFile, TAKE_BACK_IMAGE_DIR);

                    // 반품 상세 불러오기
                    OrderDetail orderDetail = orderDetailRepository.findById(item.getOrderDetailId())
                            .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.ORDER_DETAIL_NOT_FOUND));

                    return TakeBackDetail.builder()
                            .takeBackId(takeBack.getTakeBackId())
                            .productId(orderDetail.getProductId())
                            .quantity(orderDetail.getQuantity())
                            .price(orderDetail.getUnitPrice())
                            .takeBackReason(item.getTakeBackReason())
                            .takeBackImage(filePath)
                            .build();
                }).toList();

        takeBackDetailRepository.saveAll(details);

        return TakeBackCommandResponse.builder()
                .takeBackId(takeBack.getTakeBackId())
                .takeBackStatus(takeBack.getTakeBackStatus())
                .build();
    }

    // 반품 취소
    @Override
    public TakeBackCommandResponse cancelTakeBack(Long takeBackId, Long memberId) {
        // 1. 반품 및 사용자 조회
        TakeBack takeBack = takeBackRepository.findById(takeBackId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.TAKE_BACK_NOT_FOUND));
        Order order = orderRepository.findById(takeBack.getOrderId())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // 2. 사용자 및 상태 검증
        if (!order.getMemberId().equals(memberId)) {
            throw new TakeBackException(TakeBackErrorCode.UNAUTHORIZED_ACCESS_TO_ORDER);
        }
        if (!TakeBackStatus.REQUESTED.equals(takeBack.getTakeBackStatus())) {
            throw new TakeBackException(TakeBackErrorCode.INVALID_TAKE_BACK_STATUS_FOR_CANCEL);
        }

        // 3. 상태 변경
        takeBack.cancel();

        return TakeBackCommandResponse.builder()
                .takeBackId(takeBack.getTakeBackId())
                .takeBackStatus(takeBack.getTakeBackStatus())
                .build();
    }

    // 반품 수거
    @Override
    public TakeBackCommandResponse collectTakeBack(Long takeBackId, Long memberId) {
        // 1. 반품 조회
        validateWarehouseManager(takeBackId, memberId);
        TakeBack takeBack = takeBackRepository.findById(takeBackId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.TAKE_BACK_NOT_FOUND));

        // 2. 상태 검증 (REQUESTED 상태만 수거 가능)
        if (!TakeBackStatus.REQUESTED.equals(takeBack.getTakeBackStatus())) {
            throw new TakeBackException(TakeBackErrorCode.INVALID_TAKE_BACK_STATUS_FOR_COLLECT);
        }

        // 3. 상태 변경
        takeBack.collect();

        return TakeBackCommandResponse.builder()
                .takeBackId(takeBack.getTakeBackId())
                .takeBackStatus(takeBack.getTakeBackStatus())
                .build();
    }

    // 반품 승인
    @Override
    public TakeBackCommandResponse approveTakeBack(Long takeBackId, Long memberId) {
        // 1. 반품 조회
        validateWarehouseManager(takeBackId, memberId);
        TakeBack takeBack = takeBackRepository.findById(takeBackId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.TAKE_BACK_NOT_FOUND));

        // 2. 상태 검증 (COLLECTED 상태만 승인 가능)
        if (!TakeBackStatus.COLLECTED.equals(takeBack.getTakeBackStatus())) {
            throw new TakeBackException(TakeBackErrorCode.INVALID_TAKE_BACK_STATUS_FOR_APPROVE);
        }

        // 3. 상태 변경
        takeBack.approve();

        return TakeBackCommandResponse.builder()
                .takeBackId(takeBack.getTakeBackId())
                .takeBackStatus(takeBack.getTakeBackStatus())
                .build();
    }

    // 반품 반려
    @Override
    public TakeBackCommandResponse rejectTakeBack(Long takeBackId, TakeBackRejectRequest request, Long memberId) {
        // 1. 반품 조회
        validateWarehouseManager(takeBackId, memberId);
        TakeBack takeBack = takeBackRepository.findById(takeBackId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.TAKE_BACK_NOT_FOUND));

        // 2. 상태 검증 (COLLECTED 상태만 승인 가능)
        if (!TakeBackStatus.COLLECTED.equals(takeBack.getTakeBackStatus())) {
            throw new TakeBackException(TakeBackErrorCode.INVALID_TAKE_BACK_STATUS_FOR_REJECT);
        }

        // 3. 상태 변경
        takeBack.reject(request.getRejectReason());

        // 4. 사용자가 관리하는 창고 조회
        Warehouse warehouse = warehouseRepository.findByMemberIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.WAREHOUSE_NOT_FOUND));

        // 5. 재배송 요청
        Delivery redelivery = Delivery.builder()
                .orderId(takeBack.getOrderId())
                .takeBackId(takeBack.getTakeBackId())
                .warehouseId(warehouse.getWarehouseId())
                .deliveryMethod(DeliveryMethod.HEADQUARTERS)
                .deliveryStatus(DeliveryStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .build();
        deliveryRepository.save(redelivery);

        return TakeBackCommandResponse.builder()
                .takeBackId(takeBack.getTakeBackId())
                .takeBackStatus(takeBack.getTakeBackStatus())
                .build();
    }

    // 반품 코드 생성
    private String generateTakeBackCode() {
        // 오늘 날짜를 yyMMdd 형식으로 변환
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 오늘 날짜의 기존 반품 수 조회
        long count = takeBackRepository.countByCreatedAtBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );

        // 코드 중 시퀀스값 계산
        String sequencePart = String.format("%03d", count + 1);

        return "TB-" + datePart + sequencePart;
    }

    private void validateWarehouseManager(Long takeBackId, Long memberId) {
        TakeBack takeBack = takeBackRepository.findById(takeBackId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.TAKE_BACK_NOT_FOUND));

        Delivery delivery = deliveryRepository.findByOrderId(takeBack.getOrderId())
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        Warehouse warehouse = warehouseRepository.findById(delivery.getWarehouseId())
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.WAREHOUSE_NOT_FOUND));

        if (!warehouse.getMemberId().equals(memberId)) {
            throw new TakeBackException(TakeBackErrorCode.UNAUTHORIZED_ACCESS_TO_WAREHOUSE);
        }
    }


}

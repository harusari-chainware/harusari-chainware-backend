package com.harusari.chainware.takeback.command.application.service;

import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryMethod;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import com.harusari.chainware.delivery.command.domain.repository.DeliveryRepository;
import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TakeBackCommandServiceImpl implements TakeBackCommandService {

    private final TakeBackRepository takeBackRepository;
    private final TakeBackDetailRepository takeBackDetailRepository;

    private final OrderDetailRepository orderDetailRepository;
    private final DeliveryRepository deliveryRepository;
    private final WarehouseRepository warehouseRepository;

    // 반품 신청
    @Override
    public TakeBackCommandResponse createTakeBack(TakeBackCreateRequest request) {
        // 1. 반품 엔티티 저장
        String takeBackCode = generateTakeBackCode();
        TakeBack takeBack = takeBackRepository.save(TakeBack.builder()
                .orderId(request.getOrderId())
                .takeBackCode(takeBackCode)
                .build());

        // 2. 반품 상세 저장 (OrderDetail 조회 후 정보 채움)
        List<TakeBackDetail> details = request.getItems().stream().map(item -> {
            OrderDetail orderDetail = orderDetailRepository.findById(item.getOrderDetailId())
                    .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.ORDER_DETAIL_NOT_FOUND));

            return TakeBackDetail.builder()
                    .takeBackId(takeBack.getTakeBackId())
                    .productId(orderDetail.getProductId())
                    .quantity(orderDetail.getQuantity())
                    .price(orderDetail.getUnitPrice())
                    .takeBackReason(item.getTakeBackReason())
                    .takeBackImage(item.getTakeBackImage())
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
    public TakeBackCommandResponse cancelTakeBack(Long takeBackId) {
        // 1. 반품 조회
        TakeBack takeBack = takeBackRepository.findById(takeBackId)
                .orElseThrow(() -> new TakeBackException(TakeBackErrorCode.TAKE_BACK_NOT_FOUND));

        // 2. 상태 검증 (REQUESTED 상태만 취소 가능)
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
    public TakeBackCommandResponse collectTakeBack(Long takeBackId) {
        // 1. 반품 조회
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
    public TakeBackCommandResponse approveTakeBack(Long takeBackId) {
        // 1. 반품 조회
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

}

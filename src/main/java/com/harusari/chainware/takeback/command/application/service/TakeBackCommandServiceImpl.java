package com.harusari.chainware.takeback.command.application.service;

import com.harusari.chainware.takeback.command.application.dto.request.TakeBackCreateRequest;
import com.harusari.chainware.takeback.command.application.dto.response.TakeBackCommandResponse;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBack;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackDetail;
import com.harusari.chainware.takeback.command.domain.repository.TakeBackDetailRepository;
import com.harusari.chainware.takeback.command.domain.repository.TakeBackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TakeBackCommandServiceImpl implements TakeBackCommandService {

    private final TakeBackRepository takeBackRepository;
    private final TakeBackDetailRepository takeBackDetailRepository;

    @Override
    public TakeBackCommandResponse createTakeBack(TakeBackCreateRequest request) {
        // 1. 반품 엔티티 저장
        String takeBackCode = generateTakeBackCode();
        TakeBack takeBack = takeBackRepository.save(TakeBack.builder()
                .orderId(request.getOrderId())
                .takeBackCode(takeBackCode)
                .build());

        // 2. 반품 상세 저장
        List<TakeBackDetail> details = request.getItems().stream()
                .map(item -> TakeBackDetail.builder()
                        .takeBackId(takeBack.getTakeBackId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .takeBackReason(item.getTakeBackReason())
                        .takeBackImage(item.getTakeBackImage())
                        .build())
                .toList();

        takeBackDetailRepository.saveAll(details);

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

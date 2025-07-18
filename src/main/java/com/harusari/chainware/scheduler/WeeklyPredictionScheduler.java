package com.harusari.chainware.scheduler;

import com.harusari.chainware.common.properties.FastApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyPredictionScheduler {

    private final RestTemplate restTemplate;
    private final FastApiProperties fastApiProperties;

    // 매주 금요일 오전 7시에 실행
    @Scheduled(cron = "0 0 7 ? * FRI", zone = "Asia/Seoul")
    public void requestNextWeekPrediction() {
        try {
            log.info("다음 주 매출 예측 자동 요청 시작");
            String sales = restTemplate.postForObject(fastApiProperties.getFullUrl("/predict/schedule-sales-next-week"), null, String.class);
            log.info("매출 예측 완료 → {}", sales);

            log.info("다음 주 주문량 예측 자동 요청 시작");
            String order = restTemplate.postForObject(fastApiProperties.getFullUrl("/predict/schedule-order-prediction"), null, String.class);
            log.info("주문량 예측 완료 → {}", order);

            log.info("다음 주 발주량 예측 자동 요청 시작");
            String purchase = restTemplate.postForObject(fastApiProperties.getFullUrl("/predict/schedule-purchase-prediction"), null, String.class);
            log.info("발주량 예측 완료 → {}", purchase);
        } catch (Exception e) {
            log.error("예측 자동 요청 실패", e);
        }
    }

}
package com.harusari.chainware.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExternalFactorsScheduler {

    private final RestTemplate restTemplate = new RestTemplate();

    // 매주 토요일 오전 6시에 실행
    @Scheduled(cron = "0 0 6 ? * SAT", zone = "Asia/Seoul")
    public void callUpdateExternalFactors() {
        String url = "http://localhost:8000/admin/update-external-factors"; // FastAPI 서버 주소
        try {
            log.info("📡 외부요인 업데이트 요청 시작");
            String response = restTemplate.postForObject(url, null, String.class);
            log.info(" 외부요인 업데이트 완료 → 응답: {}", response);
        } catch (Exception e) {
            log.error(" 외부요인 업데이트 실패", e);
        }
    }
}

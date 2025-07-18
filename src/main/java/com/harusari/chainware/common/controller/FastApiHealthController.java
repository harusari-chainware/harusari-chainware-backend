package com.harusari.chainware.common.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.FastApiHealthResponse;
import com.harusari.chainware.common.properties.FastApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.*;

@RestController
@RequiredArgsConstructor
public class FastApiHealthController {

    private final RestTemplate restTemplate;
    private final FastApiProperties fastApiProperties;

    @GetMapping("/api/v1/health/check/fastapi")
    public ResponseEntity<ApiResponse<FastApiHealthResponse>> healthFastApi() {
        try {
            FastApiHealthResponse response = restTemplate.getForObject(
                    fastApiProperties.getFullUrl("/api/v1/health/check"),
                    FastApiHealthResponse.class
            );
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(ApiResponse.failure("FastAPI HTTP 오류", e.getMessage()));
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.failure("FastAPI 연결 실패", e.getMessage()));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("FastAPI 호출 중 알 수 없는 오류", e.getMessage()));
        }
    }

}
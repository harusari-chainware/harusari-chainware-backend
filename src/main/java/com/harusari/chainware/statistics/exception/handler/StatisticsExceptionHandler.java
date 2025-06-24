package com.harusari.chainware.statistics.exception.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class StatisticsExceptionHandler {

    @ExceptionHandler(StatisticsException.class)
    public ResponseEntity<ApiResponse<Void>> handleStatisticsException(StatisticsException e) {
        StatisticsErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}

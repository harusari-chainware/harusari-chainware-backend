package com.harusari.chainware.disposal.exception.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.disposal.exception.DisposalErrorCode;
import com.harusari.chainware.disposal.exception.DisposalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DisposalExceptionHandler {

    @ExceptionHandler(DisposalException.class)
    public ResponseEntity<ApiResponse<Void>> handleStatisticsException(DisposalException e) {
        DisposalErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}

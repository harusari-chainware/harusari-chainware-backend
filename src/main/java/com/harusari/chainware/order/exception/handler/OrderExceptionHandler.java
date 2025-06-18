package com.harusari.chainware.order.exception.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderUpdateInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(OrderUpdateInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpdateInvalidException(OrderUpdateInvalidException e) {
        OrderErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
package com.harusari.chainware.warehouse.exception.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WarehouseExceptionHandler {

    @ExceptionHandler(WarehouseException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpdateInvalidException(WarehouseException e) {
        WarehouseErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
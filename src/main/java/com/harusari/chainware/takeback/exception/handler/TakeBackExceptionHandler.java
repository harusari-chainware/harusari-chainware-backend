package com.harusari.chainware.takeback.exception.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.takeback.exception.TakeBackErrorCode;
import com.harusari.chainware.takeback.exception.TakeBackException;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TakeBackExceptionHandler {

    @ExceptionHandler(TakeBackException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpdateInvalidException(TakeBackException e) {
        TakeBackErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
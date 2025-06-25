package com.harusari.chainware.exception.purchase.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.purchase.PurchaseOrderErrorCode;
import com.harusari.chainware.exception.purchase.PurchaseOrderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PurchaseOrderExceptionHandler {

    @ExceptionHandler(PurchaseOrderException.class)
    public ResponseEntity<ApiResponse<Void>> handlePurchaseOrderException(PurchaseOrderException e) {
        PurchaseOrderErrorCode code = e.getErrorCode();
        return new ResponseEntity<>(
            ApiResponse.failure(code.getErrorCode(), code.getErrorMessage()),
            code.getHttpStatus()
        );
    }
}



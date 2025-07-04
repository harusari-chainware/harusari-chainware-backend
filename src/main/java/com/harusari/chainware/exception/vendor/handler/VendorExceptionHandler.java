package com.harusari.chainware.exception.vendor.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.vendor.VendorAgreementNotFoundException;
import com.harusari.chainware.exception.vendor.VendorErrorCode;
import com.harusari.chainware.exception.vendor.VendorNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VendorExceptionHandler {

    @ExceptionHandler(VendorNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleVendorNotFoundException(VendorNotFoundException e) {
        VendorErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(VendorAgreementNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleVendorAgreementNotFoundException(VendorAgreementNotFoundException e) {
        VendorErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
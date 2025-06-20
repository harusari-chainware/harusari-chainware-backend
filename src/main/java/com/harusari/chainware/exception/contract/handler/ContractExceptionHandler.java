 package com.harusari.chainware.exception.contract.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.contract.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContractExceptionHandler {

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleContractNotFoundException(ContractNotFoundException e) {
        var errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ContractAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleContractAlreadyExistsException(ContractAlreadyExistsException e) {
        var errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ContractCannotDeleteException.class)
    public ResponseEntity<ApiResponse<Void>> handleContractCannotDeleteException(ContractCannotDeleteException e) {
        var errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ContractPeriodInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleContractPeriodInvalidException(ContractPeriodInvalidException e) {
        var errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ContractVendorProductConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleContractVendorProductConflictException(ContractVendorProductConflictException e) {
        var errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}
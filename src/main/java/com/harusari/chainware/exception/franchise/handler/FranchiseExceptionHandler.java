package com.harusari.chainware.exception.franchise.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.franchise.FranchiseErrorCode;
import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FranchiseExceptionHandler {

    @ExceptionHandler(FranchiseNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFranchiseNotFoundException(FranchiseNotFoundException e) {
        FranchiseErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
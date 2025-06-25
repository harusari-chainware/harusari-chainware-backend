package com.harusari.chainware.exception.requisition.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.requisition.RequisitionErrorCode;
import com.harusari.chainware.exception.requisition.RequisitionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class requisitionExceptionHandler {

    @ExceptionHandler(RequisitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleRequisitionException(RequisitionException e) {
        RequisitionErrorCode code = e.getErrorCode();
        return new ResponseEntity<>(
                ApiResponse.failure(code.getErrorCode(), code.getErrorMessage()),
                code.getHttpStatus()
        );
    }
}
package com.harusari.chainware.exception.product.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.product.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFoundException(ProductNotFoundException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(InvalidProductStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidProductStatusException(InvalidProductStatusException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ProductCannotBeDeletedException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductCannotBeDeletedException(ProductCannotBeDeletedException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ProductUpdateFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductUpdateFailedException(ProductUpdateFailedException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(ProductAlreadyDeletedException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductAlreadyDeletedException(ProductAlreadyDeletedException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(DuplicateProductNameException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateProductNameException(DuplicateProductNameException e) {
        ProductErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}

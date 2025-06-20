package com.harusari.chainware.exception.category.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.category.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(TopCategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTopCategoryNotFoundException(TopCategoryNotFoundException e) {
        CategoryErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(TopCategoryNameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleTopCategoryNameAlreadyExistsException(TopCategoryNameAlreadyExistsException e) {
        CategoryErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(TopCategoryCannotDeleteHasProductsException.class)
    public ResponseEntity<ApiResponse<Void>> handleTopCategoryCannotDeleteHasProductsException(TopCategoryCannotDeleteHasProductsException e) {
        CategoryErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryNotFoundException(CategoryNotFoundException e) {
        CategoryErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CategoryNameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryNameAlreadyExistsException(CategoryNameAlreadyExistsException e) {
        CategoryErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CategoryCannotDeleteHasProductsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryCannotDeleteHasProductsException(CategoryCannotDeleteHasProductsException e) {
        CategoryErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


}
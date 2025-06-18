package com.harusari.chainware.exception.common.storage.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.common.storage.FileUploadFailedException;
import com.harusari.chainware.exception.common.storage.InvalidFileException;
import com.harusari.chainware.exception.common.storage.StorageErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StorageExceptionHandler {

    @ExceptionHandler(FileUploadFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileUploadFailedException(FileUploadFailedException e) {
        StorageErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidFileException(InvalidFileException e) {
        StorageErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
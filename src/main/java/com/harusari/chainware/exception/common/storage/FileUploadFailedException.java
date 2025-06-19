package com.harusari.chainware.exception.common.storage;

import lombok.Getter;

@Getter
public class FileUploadFailedException extends RuntimeException {

    private final StorageErrorCode errorCode;

    public FileUploadFailedException(StorageErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause);
        this.errorCode = errorCode;
    }

}
package com.harusari.chainware.exception.common.storage;

import lombok.Getter;

@Getter
public class InvalidFileException extends RuntimeException {

    private final StorageErrorCode errorCode;

    public InvalidFileException(StorageErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class DuplicateProductNameException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public DuplicateProductNameException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
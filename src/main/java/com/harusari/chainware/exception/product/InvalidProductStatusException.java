package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class InvalidProductStatusException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public InvalidProductStatusException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
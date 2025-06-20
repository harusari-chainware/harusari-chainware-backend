package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class ProductAlreadyDeletedException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public ProductAlreadyDeletedException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class ProductUpdateFailedException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public ProductUpdateFailedException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
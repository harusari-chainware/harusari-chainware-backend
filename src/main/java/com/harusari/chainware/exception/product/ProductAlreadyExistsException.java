package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class ProductAlreadyExistsException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public ProductAlreadyExistsException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
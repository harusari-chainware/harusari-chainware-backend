package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public ProductNotFoundException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
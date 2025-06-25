package com.harusari.chainware.exception.product;

import lombok.Getter;

@Getter
public class ProductCannotBeDeletedException extends RuntimeException {

    private final ProductErrorCode errorCode;

    public ProductCannotBeDeletedException(ProductErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
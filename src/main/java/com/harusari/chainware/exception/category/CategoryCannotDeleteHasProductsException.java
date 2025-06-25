package com.harusari.chainware.exception.category;

import lombok.Getter;

@Getter
public class CategoryCannotDeleteHasProductsException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public CategoryCannotDeleteHasProductsException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
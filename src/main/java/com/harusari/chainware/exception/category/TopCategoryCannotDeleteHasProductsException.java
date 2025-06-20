package com.harusari.chainware.exception.category;

import lombok.Getter;

@Getter
public class TopCategoryCannotDeleteHasProductsException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public TopCategoryCannotDeleteHasProductsException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
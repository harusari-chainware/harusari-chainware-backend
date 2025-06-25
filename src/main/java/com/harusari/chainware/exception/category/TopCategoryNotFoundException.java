package com.harusari.chainware.exception.category;

import lombok.Getter;

@Getter
public class TopCategoryNotFoundException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public TopCategoryNotFoundException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
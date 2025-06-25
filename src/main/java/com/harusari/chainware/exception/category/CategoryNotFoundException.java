package com.harusari.chainware.exception.category;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public CategoryNotFoundException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

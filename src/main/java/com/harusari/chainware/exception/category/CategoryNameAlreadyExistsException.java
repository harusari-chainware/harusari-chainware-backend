package com.harusari.chainware.exception.category;

import lombok.Getter;

@Getter
public class CategoryNameAlreadyExistsException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public CategoryNameAlreadyExistsException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
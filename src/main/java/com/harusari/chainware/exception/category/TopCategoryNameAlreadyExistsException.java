package com.harusari.chainware.exception.category;

import lombok.Getter;

@Getter
public class TopCategoryNameAlreadyExistsException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public TopCategoryNameAlreadyExistsException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
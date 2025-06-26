package com.harusari.chainware.exception.franchise;

import lombok.Getter;

@Getter
public class FranchiseNotFoundException extends RuntimeException {

    private final FranchiseErrorCode errorCode;

    public FranchiseNotFoundException(FranchiseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
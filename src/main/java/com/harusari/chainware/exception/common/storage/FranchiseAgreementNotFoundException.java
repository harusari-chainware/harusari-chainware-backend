package com.harusari.chainware.exception.common.storage;

import lombok.Getter;

@Getter
public class FranchiseAgreementNotFoundException extends RuntimeException {

    private final StorageErrorCode errorCode;

    public FranchiseAgreementNotFoundException(StorageErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
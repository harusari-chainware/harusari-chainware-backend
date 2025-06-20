package com.harusari.chainware.exception.contract;

import lombok.Getter;

@Getter
public class ContractNotFoundException extends RuntimeException {

    private final ContractErrorCode errorCode;

    public ContractNotFoundException(ContractErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
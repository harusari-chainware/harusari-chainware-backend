package com.harusari.chainware.exception.contract;

import lombok.Getter;

@Getter
public class ContractPeriodInvalidException extends RuntimeException {

    private final ContractErrorCode errorCode;

    public ContractPeriodInvalidException(ContractErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
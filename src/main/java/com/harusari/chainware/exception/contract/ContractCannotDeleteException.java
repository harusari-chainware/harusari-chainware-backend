package com.harusari.chainware.exception.contract;

import lombok.Getter;

@Getter
public class ContractCannotDeleteException extends RuntimeException {

    private final ContractErrorCode errorCode;

    public ContractCannotDeleteException(ContractErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
package com.harusari.chainware.exception.contract;

import lombok.Getter;

@Getter
public class ContractAlreadyExistsException extends RuntimeException {

    private final ContractErrorCode errorCode;

    public ContractAlreadyExistsException(ContractErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
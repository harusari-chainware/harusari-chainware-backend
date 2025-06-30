package com.harusari.chainware.exception.contract;

import lombok.Getter;

@Getter
public class ContractAccessDeniedException extends RuntimeException {

    private final ContractErrorCode errorCode;

    public ContractAccessDeniedException(ContractErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

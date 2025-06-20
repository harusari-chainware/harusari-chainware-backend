package com.harusari.chainware.exception.contract;

import lombok.Getter;

@Getter
public class ContractVendorProductConflictException extends RuntimeException {

    private final ContractErrorCode errorCode;

    public ContractVendorProductConflictException(ContractErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
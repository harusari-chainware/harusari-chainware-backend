package com.harusari.chainware.exception.requisition;

import lombok.Getter;

@Getter
public class RequisitionException extends RuntimeException {

    private final RequisitionErrorCode errorCode;

    public RequisitionException(RequisitionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
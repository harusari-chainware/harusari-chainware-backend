package com.harusari.chainware.disposal.exception;

import lombok.Getter;

@Getter
public class DisposalException extends RuntimeException {

    private final DisposalErrorCode errorCode;

    public DisposalException(DisposalErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

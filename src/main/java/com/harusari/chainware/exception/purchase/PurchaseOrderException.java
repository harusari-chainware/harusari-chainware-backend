package com.harusari.chainware.exception.purchase;

import lombok.Getter;

@Getter
public class PurchaseOrderException extends RuntimeException {

    private final PurchaseOrderErrorCode errorCode;

    public PurchaseOrderException(PurchaseOrderErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

package com.harusari.chainware.delivery.exception;

import lombok.Getter;

@Getter
public class DeliveryException extends RuntimeException {

    private final DeliveryErrorCode errorCode;

    public DeliveryException(DeliveryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
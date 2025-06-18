package com.harusari.chainware.order.exception;

import lombok.Getter;

@Getter
public class OrderUpdateInvalidException extends RuntimeException {

    private final OrderErrorCode errorCode;

    public OrderUpdateInvalidException(OrderErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
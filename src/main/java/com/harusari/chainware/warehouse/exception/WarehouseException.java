package com.harusari.chainware.warehouse.exception;

import lombok.Getter;

@Getter
public class WarehouseException extends RuntimeException {

    private final WarehouseErrorCode errorCode;

    public WarehouseException(WarehouseErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
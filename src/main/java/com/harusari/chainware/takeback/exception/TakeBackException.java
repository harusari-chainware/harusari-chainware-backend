package com.harusari.chainware.takeback.exception;

import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import lombok.Getter;

@Getter
public class TakeBackException extends RuntimeException {

    private final TakeBackErrorCode errorCode;

    public TakeBackException(TakeBackErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
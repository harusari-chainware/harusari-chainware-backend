package com.harusari.chainware.statistics.exception;

import lombok.Getter;

@Getter
public class StatisticsException extends RuntimeException {

    private final StatisticsErrorCode errorCode;

    public StatisticsException(StatisticsErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

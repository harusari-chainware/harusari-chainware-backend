package com.harusari.chainware.exception.vendor;

import lombok.Getter;

@Getter
public class VendorNotFoundException extends RuntimeException {

    private final VendorErrorCode errorCode;

    public VendorNotFoundException(VendorErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
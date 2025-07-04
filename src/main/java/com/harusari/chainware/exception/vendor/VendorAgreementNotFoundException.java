package com.harusari.chainware.exception.vendor;

import lombok.Getter;

@Getter
public class VendorAgreementNotFoundException extends RuntimeException {

    private final VendorErrorCode errorCode;

    public VendorAgreementNotFoundException(VendorErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
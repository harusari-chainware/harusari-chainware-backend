package com.harusari.chainware.member.common.constants;

import java.time.Duration;

public final class EmailValidationConstant {

    public static final String EMAIL_VALIDATION_PREFIX = "email-validation:";
    public static final Duration EMAIL_VALIDATION_TTL = Duration.ofMinutes(10);

    private EmailValidationConstant() {}

}
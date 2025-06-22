package com.harusari.chainware.member.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailValidationConstant {

    public static final String EMAIL_VALIDATION_PREFIX = "email-validation:";
    public static final Duration EMAIL_VALIDATION_TTL = Duration.ofMinutes(10);

}
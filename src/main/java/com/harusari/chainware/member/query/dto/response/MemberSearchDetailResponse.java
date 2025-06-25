package com.harusari.chainware.member.query.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record MemberSearchDetailResponse(
        Long memberId, String email, String name, String authorityLabelKr,
        String phoneNumber, LocalDate birthDate, String position,
        LocalDateTime joinAt, LocalDateTime modifiedAt, boolean isDeleted
) {
}
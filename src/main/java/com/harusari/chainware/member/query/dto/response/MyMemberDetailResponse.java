package com.harusari.chainware.member.query.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MyMemberDetailResponse(
        Long memberId, String email, String name, String authorityLabelKr,
        String phoneNumber, LocalDate birthDate, String position
) {
}
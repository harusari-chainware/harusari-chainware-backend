package com.harusari.chainware.member.command.application.dto.request.franchise;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FranchiseCreateRequest(
        String franchiseName, String franchiseContact, String franchiseTaxId,
        String franchiseAddress, LocalDate contractStartDate, LocalDate contractEndDate
) {
}
package com.harusari.chainware.member.command.application.dto.request.franchise;

import com.harusari.chainware.common.dto.AddressRequest;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FranchiseCreateRequest(
        String franchiseName, String franchiseContact, String franchiseTaxId,
        AddressRequest addressRequest, LocalDate contractStartDate, LocalDate contractEndDate
) {
}
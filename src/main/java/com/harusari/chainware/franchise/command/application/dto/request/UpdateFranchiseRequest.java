package com.harusari.chainware.franchise.command.application.dto.request;

import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateFranchiseRequest(
        String franchiseName, String franchiseContact, String franchiseTaxId, FranchiseStatus franchiseStatus,
        AddressRequest addressRequest, LocalDate contractStartDate, LocalDate contractEndDate
) {
}
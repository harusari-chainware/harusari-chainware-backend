package com.harusari.chainware.franchise.query.dto.resposne;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FranchiseSearchResponse(
        String franchiseName, String franchiseManager, String franchiseContact, Address franchiseAddress,
        FranchiseStatus franchiseStatus, LocalDate contractStartDate, LocalDate contractEndDate
) {
}
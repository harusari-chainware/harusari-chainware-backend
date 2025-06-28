package com.harusari.chainware.franchise.query.dto.request;

import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FranchiseSearchRequest(
        String franchiseName, String zipcode, String addressRoad,
        String addressDetail, FranchiseStatus franchiseStatus,
        LocalDate contractStartDate, LocalDate contractEndDate
) {
}
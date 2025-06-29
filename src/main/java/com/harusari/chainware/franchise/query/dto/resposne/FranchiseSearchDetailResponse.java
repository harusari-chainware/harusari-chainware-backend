package com.harusari.chainware.franchise.query.dto.resposne;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FranchiseSearchDetailResponse(
        Long memberId, String name, String phoneNumber, Long franchiseId, String franchiseName, String franchiseContact,
        String franchiseTaxId, Address franchiseAddress, String agreementFilePath, String agreementOriginalFileName,
        Long agreementFileSize, LocalDate contractStartDate, LocalDate contractEndDate, FranchiseStatus franchiseStatus
) {
}
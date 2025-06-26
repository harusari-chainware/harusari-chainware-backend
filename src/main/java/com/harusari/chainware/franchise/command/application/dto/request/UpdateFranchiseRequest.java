package com.harusari.chainware.franchise.command.application.dto.request;

import com.harusari.chainware.common.dto.AddressRequest;
import lombok.Builder;

@Builder
public record UpdateFranchiseRequest(
        String franchiseName, String franchiseContact,
        String franchiseTaxId, AddressRequest addressRequest
) {
}
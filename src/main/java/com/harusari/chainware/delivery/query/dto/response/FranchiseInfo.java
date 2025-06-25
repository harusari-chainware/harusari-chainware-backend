package com.harusari.chainware.delivery.query.dto.response;

import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FranchiseInfo {
    private String franchiseName;
    private String franchiseAddress;
    private String franchiseTaxId;
    private FranchiseStatus franchiseStatus;
    private String ownerName;
    private String ownerPhoneNumber;
    private String franchisePhoneNumber;
}

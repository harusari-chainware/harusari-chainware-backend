package com.harusari.chainware.order.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FranchiseOwnerInfo {
    private String ownerName;
    private String ownerPhone;
    private String franchiseName;
    private String franchisePhone;
    private String businessNumber;
}
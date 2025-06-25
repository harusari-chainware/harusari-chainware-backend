package com.harusari.chainware.order.query.dto.response;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class FranchiseOwnerInfo {
    private String ownerName;
    private String ownerPhone;
    private String franchiseName;
    private String franchisePhone;
    private String businessNumber;
}
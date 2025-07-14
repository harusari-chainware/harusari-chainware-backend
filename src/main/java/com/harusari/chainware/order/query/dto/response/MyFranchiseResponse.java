package com.harusari.chainware.order.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
@AllArgsConstructor
public class MyFranchiseResponse {
    private String franchiseName;
    private String franchiseContact;
    private String ownerName;
    private String ownerPhone;
}

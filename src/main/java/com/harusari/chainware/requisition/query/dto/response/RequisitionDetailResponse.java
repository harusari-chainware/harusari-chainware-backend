package com.harusari.chainware.requisition.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RequisitionDetailResponse {
    private RequisitionInfo requisitionInfo;
    private MemberInfo createdMember;
    private MemberInfo approvedMember;
    private VendorInfo vendor;
    private List<RequisitionItemInfo> items;
}
package com.harusari.chainware.requisition.query.dto.response;

import com.harusari.chainware.purchase.query.dto.MemberInfo;
import com.harusari.chainware.purchase.query.dto.PWarehouseInfo;
import com.harusari.chainware.purchase.query.dto.VendorInfo;
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
    private PWarehouseInfo warehouse;
    private List<RequisitionItemInfo> items;
}
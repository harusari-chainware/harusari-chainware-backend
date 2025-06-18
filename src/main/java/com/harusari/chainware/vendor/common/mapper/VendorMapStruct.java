package com.harusari.chainware.vendor.common.mapper;

import com.harusari.chainware.member.command.application.dto.request.vendor.VendorCreateRequest;
import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy =  ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface VendorMapStruct {

    Vendor toVendor(VendorCreateRequest vendorCreateRequest, Long memberId);

}
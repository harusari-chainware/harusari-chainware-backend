package com.harusari.chainware.vendor.common.mapstruct;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.member.command.application.dto.request.vendor.VendorCreateRequest;
import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy =  ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VendorMapStruct {

    @Mapping(
            target = "vendorAddress",
            source = "vendorCreateRequest.addressRequest",
            qualifiedByName = "addressRequestToAddress"
    )
    Vendor toVendor(VendorCreateRequest vendorCreateRequest, Long memberId);

    @Named("addressRequestToAddress")
    default Address addressRequestToAddress(AddressRequest request) {
        if (request == null) {
            return null;
        }
        return Address.builder()
                .zipcode(request.zipcode())
                .addressRoad(request.addressRoad())
                .addressDetail(request.addressDetail())
                .build();
    }

}
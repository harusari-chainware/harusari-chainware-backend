package com.harusari.chainware.franchise.common.mapstruct;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.member.command.application.dto.request.franchise.FranchiseCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FranchiseMapStruct {

    @Mapping(
            target = "franchiseAddress",
            source = "franchiseCreateRequest.addressRequest",
            qualifiedByName = "addressRequestToAddress"
    )
    Franchise toFranchise(FranchiseCreateRequest franchiseCreateRequest, Long memberId);

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
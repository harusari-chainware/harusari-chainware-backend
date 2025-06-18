package com.harusari.chainware.member.command.application.dto.request.vendor;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import lombok.Builder;

@Builder
public record MemberWithVendorRequest(
        MemberCreateRequest memberCreateRequest,
        VendorCreateRequest vendorCreateRequest
) {
}
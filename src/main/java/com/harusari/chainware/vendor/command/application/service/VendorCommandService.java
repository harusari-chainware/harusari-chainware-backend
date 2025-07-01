package com.harusari.chainware.vendor.command.application.service;

import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.vendor.command.application.dto.VendorStatusChangeRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface VendorCommandService {

    void createVendorWithAgreement(Long memberId, MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile);
    void updateVendor(Long vendorId, VendorUpdateRequestDto dto);
    void changeVendorStatus(Long vendorId, VendorStatusChangeRequestDto dto);

}
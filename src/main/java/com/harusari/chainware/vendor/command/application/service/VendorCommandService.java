package com.harusari.chainware.vendor.command.application.service;

import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface VendorCommandService {

    void createVendorWithAgreement(Long memberId, MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile);

    void updateVendor(Long vendorId, VendorUpdateRequest dto, MultipartFile agreementFile);

}
package com.harusari.chainware.member.command.application.service;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.member.command.application.dto.request.warehouse.MemberWithWarehouseRequest;
import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {

    EmailExistsResponse checkEmailDuplicate(String email);

    void registerHeadquartersMember(MemberCreateRequest memberCreateRequest);

    void registerFranchise(MemberWithFranchiseRequest memberWithFranchiseRequest, MultipartFile agreementFile);

    void registerVendor(MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile);

    void registerWarehouse(MemberWithWarehouseRequest memberWithWarehouseRequest);

}
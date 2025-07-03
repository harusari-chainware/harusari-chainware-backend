package com.harusari.chainware.member.command.application.service;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.application.dto.request.PasswordChangeRequest;
import com.harusari.chainware.member.command.application.dto.request.UpdateMemberRequest;
import com.harusari.chainware.member.command.application.dto.request.UpdateMyInfoRequest;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.member.command.application.dto.request.warehouse.MemberWithWarehouseRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {

    void registerHeadquartersMember(MemberCreateRequest memberCreateRequest);

    void registerFranchise(MemberWithFranchiseRequest memberWithFranchiseRequest, MultipartFile agreementFile);

    void registerVendor(MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile);

    void registerWarehouse(MemberWithWarehouseRequest memberWithWarehouseRequest);

    void changePassword(PasswordChangeRequest passwordChangeRequest, String email);

    void updateMemberInfo(Long memberId, UpdateMemberRequest updateMemberRequest);

    void updateMyInfo(Long memberId, UpdateMyInfoRequest updateMyInfoRequest);

    void deleteMemberRequest(Long memberId);

}
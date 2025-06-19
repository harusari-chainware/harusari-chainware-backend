package com.harusari.chainware.franchise.command.application.service;

import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FranchiseCommandService {

    void createFranchiseWithAgreement(Long memberId, MemberWithFranchiseRequest memberWithFranchiseRequest, MultipartFile agreementFile);

}
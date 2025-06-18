package com.harusari.chainware.franchise.command.application.service;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.common.infrastructure.storage.StorageUploader;
import com.harusari.chainware.franchise.common.mapper.FranchiseMapStruct;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class FranchiseCommandServiceImpl implements FranchiseCommandService {

    private static final String FRANCHISE_DIRECTORY_NAME = "franchises";

    private final FranchiseMapStruct franchiseMapStruct;
    private final StorageUploader s3Uploader;
    private final FranchiseRepository franchiseRepository;

    @Override
    public void createFranchiseWithAgreement(
            Long memberId, MemberWithFranchiseRequest memberWithFranchiseRequest, MultipartFile agreementFile
    ) {
        Franchise franchise = franchiseMapStruct.toFranchise(memberWithFranchiseRequest.franchiseCreateRequest(), memberId);
        String filePath = s3Uploader.uploadAgreement(agreementFile, FRANCHISE_DIRECTORY_NAME);
        franchise.updateAgreementInfo(
                filePath,
                agreementFile.getOriginalFilename(),
                agreementFile.getSize(),
                LocalDateTime.now().withNano(0)
        );
        franchiseRepository.save(franchise);
    }

}
package com.harusari.chainware.franchise.command.application.service;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.mapstruct.AddressMapStruct;
import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import com.harusari.chainware.franchise.command.application.dto.request.UpdateFranchiseRequest;
import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.common.infrastructure.storage.StorageUploader;
import com.harusari.chainware.franchise.common.mapstruct.FranchiseMapStruct;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.harusari.chainware.exception.franchise.FranchiseErrorCode.FRANCHISE_NOT_FOUND_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class FranchiseCommandServiceImpl implements FranchiseCommandService {

    private static final String FRANCHISE_DIRECTORY_NAME = "franchises";

    private final StorageUploader s3Uploader;
    private final AddressMapStruct addressMapStruct;
    private final FranchiseMapStruct franchiseMapStruct;
    private final FranchiseRepository franchiseRepository;

    @Override
    public void createFranchiseWithAgreement(
            Long memberId, MemberWithFranchiseRequest memberWithFranchiseRequest, MultipartFile agreementFile
    ) {
        Franchise franchise = franchiseMapStruct.toFranchise(memberWithFranchiseRequest.franchiseCreateRequest(), memberId);
        applyAgreementFileToFranchise(agreementFile, franchise);
        franchiseRepository.save(franchise);
    }

    @Override
    public void updateFranchise(
            Long franchiseId, UpdateFranchiseRequest updateFranchiseRequest, MultipartFile agreementFile
    ) {
        Franchise franchise = franchiseRepository.findFranchiseByFranchiseId(franchiseId)
                .orElseThrow(() -> new FranchiseNotFoundException(FRANCHISE_NOT_FOUND_EXCEPTION));

        Address address = addressMapStruct.toAddress(updateFranchiseRequest.addressRequest());

        franchise.updateFranchise(
                updateFranchiseRequest.franchiseName(), updateFranchiseRequest.franchiseContact(),
                updateFranchiseRequest.franchiseTaxId(), address
        );

        if (agreementFile != null && !agreementFile.isEmpty()) {
            applyAgreementFileToFranchise(agreementFile, franchise);
        }
    }

    private void applyAgreementFileToFranchise(MultipartFile agreementFile, Franchise franchise) {
        String filePath = s3Uploader.uploadAgreement(agreementFile, FRANCHISE_DIRECTORY_NAME);
        franchise.updateAgreementInfo(
                filePath, agreementFile.getOriginalFilename(),
                agreementFile.getSize(), LocalDateTime.now().withNano(0)
        );
    }

}
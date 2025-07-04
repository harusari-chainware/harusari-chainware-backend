package com.harusari.chainware.vendor.command.application.service;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.infrastructure.storage.StorageUploader;
import com.harusari.chainware.common.mapstruct.AddressMapStruct;
import com.harusari.chainware.exception.vendor.VendorNotFoundException;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequest;
import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import com.harusari.chainware.vendor.command.domain.repository.VendorRepository;
import com.harusari.chainware.vendor.common.mapstruct.VendorMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.harusari.chainware.exception.vendor.VendorErrorCode.VENDOR_NOT_FOUND_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class VendorCommandServiceImpl implements VendorCommandService {

    private static final String VENDOR_DIRECTORY_NAME = "vendors";

    private final StorageUploader s3Uploader;
    private final VendorMapStruct vendorMapStruct;
    private final AddressMapStruct addressMapStruct;
    private final VendorRepository vendorRepository;

    @Override
    public void createVendorWithAgreement(
            Long memberId, MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile
    ) {
        Vendor vendor = vendorMapStruct.toVendor(memberWithVendorRequest.vendorCreateRequest(), memberId);
        applyAgreementFileToVendor(agreementFile, vendor);
        vendorRepository.save(vendor);
    }

    @Override
    public void updateVendor(Long vendorId, VendorUpdateRequest vendorUpdateRequest, MultipartFile agreementFile) {
        Vendor vendor = vendorRepository.findVendorByVendorId(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(VENDOR_NOT_FOUND_EXCEPTION));

        Address address = addressMapStruct.toAddress(vendorUpdateRequest.addressRequest());

        vendor.updateVendor(
                vendorUpdateRequest.vendorName(), vendorUpdateRequest.vendorType(),
                vendorUpdateRequest.vendorTaxId(), address,
                vendorUpdateRequest.vendorMemo(), vendorUpdateRequest.vendorStatus(),
                vendorUpdateRequest.vendorStartDate(), vendorUpdateRequest.vendorEndDate()
        );

        if (agreementFile != null && !agreementFile.isEmpty()) {
            applyAgreementFileToVendor(agreementFile, vendor);
        }

    }

    private void applyAgreementFileToVendor(MultipartFile agreementFile, Vendor vendor) {
        String filePath = s3Uploader.uploadAgreement(agreementFile, VENDOR_DIRECTORY_NAME);
        vendor.updateAgreementInfo(
                filePath, agreementFile.getOriginalFilename(),
                agreementFile.getSize(), LocalDateTime.now().withNano(0)
        );
        vendorRepository.save(vendor);
    }

}
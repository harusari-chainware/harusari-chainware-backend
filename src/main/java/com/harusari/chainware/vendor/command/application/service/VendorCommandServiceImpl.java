package com.harusari.chainware.vendor.command.application.service;

import com.harusari.chainware.common.infrastructure.storage.StorageUploader;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.vendor.command.application.dto.VendorCreateRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorStatusChangeRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequestDto;
import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import com.harusari.chainware.vendor.command.domain.repository.VendorRepository;
import com.harusari.chainware.vendor.common.mapstruct.VendorMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class VendorCommandServiceImpl implements VendorCommandService {

    private static final String VENDOR_DIRECTORY_NAME = "vendors";

    private final VendorMapStruct vendorMapStruct;
    private final StorageUploader s3Uploader;
    private final VendorRepository vendorRepository;

    @Override
    public Long createVendor(VendorCreateRequestDto dto) {
//        Vendor vendor = Vendor.builder()
//                .memberId(dto.memberId())
//                .vendorName(dto.vendorName())
//                .vendorType(dto.vendorType())
//                .vendorAddress(dto.vendorAddress())
//                .vendorTaxId(dto.vendorTaxId())
//                .vendorMemo(dto.vendorMemo())
//                .vendorStatus(dto.vendorStatus())
//                .agreement(dto.agreement())
//                .vendorStartDate(dto.vendorStartDate())
//                .vendorEndDate(dto.vendorEndDate())
//                .build();

//        return vendorRepository.save(vendor).getVendorId();
        return null;
    }

    @Override
    public void updateVendor(Long vendorId, VendorUpdateRequestDto dto) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래처입니다. ID: " + vendorId));

//        vendor.update(
//                dto.vendorName(),
//                dto.vendorType(),
//                dto.vendorAddress(),
//                dto.vendorTaxId(),
//                dto.vendorMemo(),
//                dto.vendorStatus(),
//                dto.agreement(),
//                dto.vendorStartDate(),
//                dto.vendorEndDate()
//        );
    }

    @Override
    public void changeVendorStatus(Long vendorId, VendorStatusChangeRequestDto dto) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래처가 존재하지 않습니다."));

//        vendor.changeStatus(dto.vendorStatus());
        vendorRepository.save(vendor);
    }

    @Override
    public void createVendorWithAgreement(
            Long memberId, MemberWithVendorRequest memberWithVendorRequest, MultipartFile agreementFile
    ) {
        Vendor vendor = vendorMapStruct.toVendor(memberWithVendorRequest.vendorCreateRequest(), memberId);
        String filePath = s3Uploader.uploadAgreement(agreementFile, VENDOR_DIRECTORY_NAME);
        vendor.updateAgreementInfo(
                filePath,
                agreementFile.getOriginalFilename(),
                agreementFile.getSize(),
                LocalDateTime.now().withNano(0)
        );
        vendorRepository.save(vendor);
    }

}
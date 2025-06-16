package com.harusari.chainware.vendor.command.application.service;

import com.harusari.chainware.vendor.command.application.dto.VendorCreateRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorStatusChangeRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequestDto;
import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import com.harusari.chainware.vendor.command.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendorCommandServiceImpl implements VendorCommandService {

    private final VendorRepository vendorRepository;

    @Override
    @Transactional
    public Long createVendor(VendorCreateRequestDto dto) {
        Vendor vendor = Vendor.builder()
                .memberId(dto.memberId())
                .vendorName(dto.vendorName())
                .vendorContact(dto.vendorContact())
                .vendorType(dto.vendorType())
                .vendorAddress(dto.vendorAddress())
                .vendorTaxId(dto.vendorTaxId())
                .vendorMemo(dto.vendorMemo())
                .vendorStatus(dto.vendorStatus())
                .agreement(dto.agreement())
                .vendorStartDate(dto.vendorStartDate())
                .vendorEndDate(dto.vendorEndDate())
                .build();

        return vendorRepository.save(vendor).getVendorId();
    }

    @Transactional
    public void updateVendor(Long vendorId, VendorUpdateRequestDto dto) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래처입니다. ID: " + vendorId));

        vendor.update(
                dto.vendorName(),
                dto.vendorContact(),
                dto.vendorType(),
                dto.vendorAddress(),
                dto.vendorTaxId(),
                dto.vendorMemo(),
                dto.vendorStatus(),
                dto.agreement(),
                dto.vendorStartDate(),
                dto.vendorEndDate()
        );
    }

    @Transactional
    public void changeVendorStatus(Long vendorId, VendorStatusChangeRequestDto dto) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 거래처가 존재하지 않습니다."));

        vendor.changeStatus(dto.vendorStatus());
        vendorRepository.save(vendor);
    }

}

package com.harusari.chainware.vendor.query.service;

import com.harusari.chainware.common.infrastructure.storage.StorageDownloader;
import com.harusari.chainware.exception.vendor.VendorAgreementNotFoundException;
import com.harusari.chainware.exception.vendor.VendorNotFoundException;
import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import com.harusari.chainware.vendor.query.dto.response.VendorContractInfoResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorPresignedUrlResponse;
import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import com.harusari.chainware.vendor.query.repository.VendorQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.harusari.chainware.exception.vendor.VendorErrorCode.VENDOR_AGREEMENT_NOT_FOUND;
import static com.harusari.chainware.exception.vendor.VendorErrorCode.VENDOR_NOT_FOUND_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorQueryServiceImpl implements VendorQueryService {

    private final StorageDownloader s3Downloader;
    private final VendorQueryRepository vendorQueryRepository;

    @Override
    public Page<VendorSearchResponse> searchVendors(VendorSearchRequest vendorSearchRequest, Pageable pageable) {
        return vendorQueryRepository.pageVendors(vendorSearchRequest, pageable);
    }

    @Override
    public VendorDetailResponse getVendorDetail(Long vendorId) {
        return vendorQueryRepository.findVendorDetailByVendorId(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(VENDOR_NOT_FOUND_EXCEPTION));
    }

    @Override
    public VendorPresignedUrlResponse generateDownloadUrl(Long vendorId) {
        Vendor vendor = vendorQueryRepository.findVendorByVendorId(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(VENDOR_NOT_FOUND_EXCEPTION));

        String s3Key = vendor.getAgreementFilePath();

        if (s3Key == null || s3Key.isBlank()) {
            throw new VendorAgreementNotFoundException(VENDOR_AGREEMENT_NOT_FOUND);
        }

        return VendorPresignedUrlResponse.builder()
                .presignedUrl(s3Downloader.generatePresignedUrl(s3Key, Duration.ofMinutes(5)))
                .build();
    }

    @Override
    public VendorContractInfoResponse getVendorContractInfo(String vendorName) {
        return vendorQueryRepository.findVendorContractInfoByVendorName(vendorName)
                .orElseThrow(() -> new VendorNotFoundException(VENDOR_NOT_FOUND_EXCEPTION));
    }

}
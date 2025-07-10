package com.harusari.chainware.franchise.query.service;

import com.harusari.chainware.common.infrastructure.storage.StorageDownloader;
import com.harusari.chainware.exception.common.storage.FranchiseAgreementNotFoundException;
import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchisePresignedUrlResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSimpleResponse;
import com.harusari.chainware.franchise.query.repository.FranchiseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static com.harusari.chainware.exception.common.storage.StorageErrorCode.FRANCHISE_AGREEMENT_NOT_FOUND;
import static com.harusari.chainware.exception.franchise.FranchiseErrorCode.FRANCHISE_NOT_FOUND_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FranchiseQueryServiceImpl implements FranchiseQueryService {

    private final StorageDownloader s3Downloader;
    private final FranchiseQueryRepository franchiseQueryRepository;

    @Override
    public Page<FranchiseSearchResponse> searchFranchises(FranchiseSearchRequest franchiseSearchRequest, Pageable pageable) {
        return franchiseQueryRepository.pageFranchises(franchiseSearchRequest, pageable);
    }

    @Override
    public FranchiseSearchDetailResponse getFranchiseDetail(Long franchiseId) {
        return franchiseQueryRepository.findFranchiseDetailById(franchiseId)
                .orElseThrow(() -> new FranchiseNotFoundException(FRANCHISE_NOT_FOUND_EXCEPTION));
    }

    @Override
    public FranchisePresignedUrlResponse generateDownloadUrl(Long franchiseId) {
        Franchise franchise = franchiseQueryRepository.findFranchiseByFranchiseId(franchiseId)
                .orElseThrow(() -> new FranchiseNotFoundException(FRANCHISE_NOT_FOUND_EXCEPTION));

        String s3Key = franchise.getAgreementFilePath();

        if (s3Key == null || s3Key.isBlank()) {
            throw new FranchiseAgreementNotFoundException(FRANCHISE_AGREEMENT_NOT_FOUND);
        }

        return FranchisePresignedUrlResponse.builder()
                .presignedUrl(s3Downloader.generatePresignedUrl(s3Key, Duration.ofMinutes(5)))
                .build();
    }

    @Override
    public List<FranchiseSimpleResponse> getAllFranchises() {
        return franchiseQueryRepository.findAllFranchiseSimple();
    }

}
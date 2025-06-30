package com.harusari.chainware.franchise.query.service;

import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.repository.FranchiseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.harusari.chainware.exception.franchise.FranchiseErrorCode.FRANCHISE_NOT_FOUND_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FranchiseQueryServiceImpl implements FranchiseQueryService {

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

}
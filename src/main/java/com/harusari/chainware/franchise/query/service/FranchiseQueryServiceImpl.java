package com.harusari.chainware.franchise.query.service;

import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.repository.FranchiseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FranchiseQueryServiceImpl implements FranchiseQueryService {

    private final FranchiseQueryRepository franchiseQueryRepository;

    @Override
    public Page<FranchiseSearchResponse> searchFranchises(FranchiseSearchRequest franchiseSearchRequest, Pageable pageable) {
        return franchiseQueryRepository.pageFranchises(franchiseSearchRequest, pageable);
    }

}
package com.harusari.chainware.franchise.query.repository;

import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FranchiseQueryRepositoryCustom {

    Page<FranchiseSearchResponse> pageFranchises(FranchiseSearchRequest franchiseSearchRequest, Pageable pageable);

    Optional<FranchiseSearchDetailResponse> findFranchiseDetailById(Long franchiseId);

}
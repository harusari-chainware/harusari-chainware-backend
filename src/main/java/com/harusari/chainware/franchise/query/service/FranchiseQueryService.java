package com.harusari.chainware.franchise.query.service;

import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchisePresignedUrlResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FranchiseQueryService {

    Page<FranchiseSearchResponse> searchFranchises(FranchiseSearchRequest franchiseSearchRequest, Pageable pageable);

    FranchiseSearchDetailResponse getFranchiseDetail(Long franchiseId);

    FranchisePresignedUrlResponse generateDownloadUrl(Long franchiseId);

    List<FranchiseSimpleResponse> getAllFranchises();

}
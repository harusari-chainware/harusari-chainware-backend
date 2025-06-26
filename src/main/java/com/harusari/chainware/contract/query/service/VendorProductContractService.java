package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListResponse;

import java.util.List;

public interface VendorProductContractService {

    // 전체 목록 조회 (권한 검증 포함)
    VendorProductContractListResponse getAllContracts(VendorProductContractSearchRequest request, CustomUserDetails userDetails);

    // 특정 거래처 기준 목록 조회
    List<VendorProductContractDto> getContractsByVendorId(Long vendorId);
}
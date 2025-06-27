package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;

public interface VendorProductContractService {
    PagedResult<VendorProductContractDto> getContracts(VendorProductContractSearchRequest request, Long memberId, boolean isManager);
}
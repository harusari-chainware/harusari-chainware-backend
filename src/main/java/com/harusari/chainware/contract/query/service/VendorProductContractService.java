package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListDto;

public interface VendorProductContractService {
    PagedResult<VendorProductContractListDto> getContracts(VendorProductContractSearchRequest request, Long memberId, boolean isManager);

    VendorProductContractDto getContractById(Long contractId, Long memberId, boolean isManager);
}
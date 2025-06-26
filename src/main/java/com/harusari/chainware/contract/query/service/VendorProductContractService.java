package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListResponse;

import java.util.List;

public interface VendorProductContractService {

//    List<VendorProductContractDto> getAllContracts();

    VendorProductContractListResponse getAllContracts(VendorProductContractSearchRequest request, boolean isManager);

    List<VendorProductContractDto> getContractsByVendorId(Long vendorId);
}
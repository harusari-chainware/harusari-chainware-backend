package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;

import java.util.List;

public interface VendorProductContractService {

    List<VendorProductContractDto> getAllContracts();

    List<VendorProductContractDto> getContractsByVendorId(Long vendorId);
}
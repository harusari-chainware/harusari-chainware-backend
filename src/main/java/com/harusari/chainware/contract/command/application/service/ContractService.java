package com.harusari.chainware.contract.command.application.service;

import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;

import java.util.List;

public interface ContractService {

    ContractResponse createContract(ContractCreateRequest request);

    ContractResponse updateContract(Long contractId, ContractUpdateRequest request);

    void deleteContract(Long contractId);

    ContractResponse getContractById(Long contractId);

    List<ContractResponse> getContractsByVendor(Long vendorId);

    List<ContractResponse> getContractsByProduct(Long productId);
}

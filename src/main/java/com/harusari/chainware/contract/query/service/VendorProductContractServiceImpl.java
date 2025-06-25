package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.mapper.VendorProductContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorProductContractServiceImpl implements VendorProductContractService {

    private final VendorProductContractMapper contractMapper;

    @Override
    public List<VendorProductContractDto> getAllContracts() {
        return contractMapper.findAllVendorProductContracts();
    }

    @Override
    public List<VendorProductContractDto> getContractsByVendorId(Long vendorId) {
        return contractMapper.findVendorProductContractsByVendorId(vendorId);
    }
}
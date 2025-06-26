package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.mapper.VendorProductContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorProductContractServiceImpl implements VendorProductContractService {

    private final VendorProductContractMapper vendorProductContractMapper;

    @Override
    public List<VendorProductContractDto> getAllContracts() {
        return vendorProductContractMapper.findAllVendorProductContracts();
    }

    @Override
    public List<VendorProductContractDto> getContractsByVendorId(Long vendorId) {
        return vendorProductContractMapper.findVendorProductContractsByVendorId(vendorId);
    }
}
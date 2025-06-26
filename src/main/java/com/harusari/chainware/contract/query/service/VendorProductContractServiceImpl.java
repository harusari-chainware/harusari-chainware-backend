package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListResponse;
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

//    @Override
//    public List<VendorProductContractDto> getAllContracts() {
//        return vendorProductContractMapper.findAllVendorProductContracts();
//    }

    @Override
    public VendorProductContractListResponse getAllContracts(VendorProductContractSearchRequest request, boolean isManager) {
        List<VendorProductContractDto> list = vendorProductContractMapper.findVendorProductContracts(request, isManager);
        long total = vendorProductContractMapper.countVendorProductContracts(request, isManager);
        int totalPages = (int) Math.ceil((double) total / request.getSize());

        return VendorProductContractListResponse.builder()
                .contracts(list)
                .pagination(Pagination.of(request.getPage(), totalPages, total))
                .build();
    }

    @Override
    public List<VendorProductContractDto> getContractsByVendorId(Long vendorId) {
        return vendorProductContractMapper.findVendorProductContractsByVendorId(vendorId);
    }
}
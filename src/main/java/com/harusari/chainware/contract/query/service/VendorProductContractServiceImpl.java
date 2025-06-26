package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.contract.command.domain.aggregate.Contract;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListResponse;
import com.harusari.chainware.contract.query.mapper.VendorProductContractMapper;
import com.harusari.chainware.exception.contract.ContractErrorCode;
import com.harusari.chainware.exception.contract.ContractNotFoundException;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.vendor.query.service.VendorQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorProductContractServiceImpl implements VendorProductContractService {

    private final VendorProductContractMapper vendorProductContractMapper;
    private final VendorQueryService vendorQueryService;

//    @Override
//    public List<VendorProductContractDto> getAllContracts() {
//        return vendorProductContractMapper.findAllVendorProductContracts();
//    }

    @Override
    public VendorProductContractListResponse getAllContracts(VendorProductContractSearchRequest request, CustomUserDetails userDetails) {
        MemberAuthorityType authority = userDetails.getMemberAuthorityType();
        boolean isManager = Set.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER).contains(authority);

        if (!isManager) {
            if (!vendorQueryService.existsByVendorIdAndMemberId(request.getVendorId(), userDetails.getMemberId())) {
                throw new ContractNotFoundException(ContractErrorCode.CONTRACT_ACCESS_DENIED);
            }
        }

        List<VendorProductContractDto> list = vendorProductContractMapper.findVendorProductContracts(request, isManager);
        long total = vendorProductContractMapper.countVendorProductContracts(request, isManager);

        return VendorProductContractListResponse.builder()
                .contracts(list)
                .pagination(Pagination.of(request.getPage(), (int) Math.ceil((double) total / request.getSize()), total))
                .build();
    }

    @Override
    public List<VendorProductContractDto> getContractsByVendorId(Long vendorId) {
        return vendorProductContractMapper.findVendorProductContractsByVendorId(vendorId);
    }
}
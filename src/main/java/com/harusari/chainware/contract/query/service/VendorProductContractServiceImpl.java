package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
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

    private final VendorProductContractMapper mapper;

    @Override
    public PagedResult<VendorProductContractDto> getContracts(VendorProductContractSearchRequest request, Long memberId, boolean isManager) {
        Long vendorId = isManager ? null : mapper.findVendorIdByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 거래처 정보가 존재하지 않습니다."));

        List<VendorProductContractDto> content = mapper.findVendorProductContracts(request, vendorId, isManager);
        long total = mapper.countVendorProductContracts(request, vendorId, isManager);

        return PagedResult.<VendorProductContractDto>builder()
                .content(content)
                .pagination(PagedResult.PaginationMeta.builder()
                        .page(request.getPage())
                        .size(request.getSize())
                        .totalElements(total)
                        .totalPages((int) Math.ceil((double) total / request.getSize()))
                        .build())
                .build();
    }
}
package com.harusari.chainware.disposal.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.disposal.exception.DisposalErrorCode;
import com.harusari.chainware.disposal.exception.DisposalException;
import com.harusari.chainware.disposal.query.dto.DisposalListDto;
import com.harusari.chainware.disposal.query.dto.DisposalListResponseDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import com.harusari.chainware.disposal.query.mapper.DisposalQueryMapper;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import com.harusari.chainware.member.command.domain.repository.MemberCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalQueryServiceImpl implements DisposalQueryService {

    private final DisposalQueryMapper mapper;
    private final FranchiseRepository franchiseRepository;
    private final WarehouseRepository warehouseRepository;
    private final MemberCommandRepository memberCommandRepository;

    @Override
    @Transactional
    public DisposalListResponseDto getDisposals(DisposalSearchRequestDto request, Long memberId, MemberAuthorityType authorityType) {
        Long franchiseId = null;
        Long warehouseId = null;

        switch (authorityType) {
            case FRANCHISE_MANAGER -> franchiseId = franchiseRepository.findFranchiseIdByMemberId(memberId)
                    .orElseThrow(() -> new DisposalException(DisposalErrorCode.DISPOSAL_QUERY_NO_FRANCHISE))
                    .getFranchiseId();

            case WAREHOUSE_MANAGER -> warehouseId = warehouseRepository.findWarehouseIdByMemberId(memberId)
                    .orElseThrow(() -> new DisposalException(DisposalErrorCode.DISPOSAL_QUERY_NO_WAREHOUSE))
                    .getWarehouseId();

            case GENERAL_MANAGER, SENIOR_MANAGER, MASTER -> {
                if (!memberCommandRepository.existsById(memberId)) {
                    throw new DisposalException(DisposalErrorCode.DISPOSAL_QUERY_UNAUTHORIZED);
                }
            }

            default -> throw new DisposalException(DisposalErrorCode.DISPOSAL_QUERY_UNAUTHORIZED);
        }

        List<DisposalListDto> items = mapper.findDisposals(request, franchiseId, warehouseId);
        long totalCount = mapper.countDisposals(request, franchiseId, warehouseId);
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());

        return DisposalListResponseDto.builder()
                .items(items)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalPages(totalPages)
                        .totalItems(totalCount)
                        .build())
                .build();
    }

}

package com.harusari.chainware.disposal.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.disposal.query.dto.DisposalListDto;
import com.harusari.chainware.disposal.query.dto.DisposalListResponseDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import com.harusari.chainware.disposal.query.mapper.DisposalQueryMapper;
import com.harusari.chainware.franchise.command.infrastructure.repository.JpaFranchiseRepository;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.member.command.infrastructure.repository.JpaMemberCommandRepository;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalQueryServiceImpl implements DisposalQueryService {

    private final DisposalQueryMapper mapper;
    private final JpaFranchiseRepository franchiseRepository;
    private final JpaWarehouseRepository warehouseRepository;
    private final JpaMemberCommandRepository memberCommandRepository;

    @Override
    @Transactional
    public DisposalListResponseDto getDisposals(DisposalSearchRequestDto request, Long memberId, MemberAuthorityType authorityType) {
        Long franchiseId = null;
        Long warehouseId = null;

        switch (authorityType) {
            case FRANCHISE_MANAGER -> franchiseId = franchiseRepository.findFranchiseIdByMemberId(memberId)
                    .orElseThrow(() -> new RuntimeException("가맹점 없음")).getFranchiseId();
            case WAREHOUSE_MANAGER -> warehouseId = warehouseRepository.findWarehouseIdByMemberId(memberId)
                    .orElseThrow(() -> new RuntimeException("창고 없음")).getWarehouseId();
            case GENERAL_MANAGER, SENIOR_MANAGER, MASTER -> {
                if (!memberCommandRepository.existsById(memberId)) {
                    throw new RuntimeException("폐기 조회 권한 없음");
                }
            }
            default -> throw new RuntimeException("폐기 조회 권한이 없는 사용자입니다.");
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

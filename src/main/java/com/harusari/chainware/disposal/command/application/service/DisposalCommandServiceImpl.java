package com.harusari.chainware.disposal.command.application.service;

import com.harusari.chainware.disposal.command.application.dto.DisposalCommandRequestDto;
import com.harusari.chainware.disposal.command.domain.aggregate.Disposal;
import com.harusari.chainware.disposal.command.infrastructure.repository.JpaDisposalRepository;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DisposalCommandServiceImpl implements DisposalCommandService {

    private final JpaDisposalRepository disposalRepository;
    private final FranchiseRepository franchiseRepository;
    private final WarehouseRepository warehouseRepository;

    @Transactional
    @Override
    public void registerDisposal(DisposalCommandRequestDto request, Long memberId, MemberAuthorityType authorityType) {
        Long franchiseId = null;
        Long warehouseId = null;

        switch (authorityType) {
            case FRANCHISE_MANAGER -> {
                franchiseId = franchiseRepository.findFranchiseIdByMemberId(memberId)
                        .orElseThrow(() -> new RuntimeException("해당 가맹점 정보를 찾을 수 없습니다.")).getFranchiseId();
            }
            case WAREHOUSE_MANAGER -> {
                warehouseId = warehouseRepository.findWarehouseIdByMemberId(memberId)
                        .orElseThrow(() -> new RuntimeException("해당 창고 정보를 찾을 수 없습니다.")).getWarehouseId();
            }
            default -> throw new RuntimeException("폐기를 등록할 권한이 없습니다.");
        }

        Disposal disposal = Disposal.builder()
                .productId(request.getProductId())
                .warehouseId(warehouseId)
                .franchiseId(franchiseId)
                .takeBackId(request.getTakeBackId())
                .quantity(request.getQuantity())
                .disposalReason(request.getDisposalReason())
                .build();

        disposalRepository.save(disposal);

        // TODO: 재고 감소 처리 (추후 구현)
    }
}

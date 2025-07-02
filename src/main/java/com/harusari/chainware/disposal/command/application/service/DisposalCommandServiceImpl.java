package com.harusari.chainware.disposal.command.application.service;

import com.harusari.chainware.disposal.command.application.dto.DisposalCommandRequestDto;
import com.harusari.chainware.disposal.command.domain.aggregate.Disposal;
import com.harusari.chainware.disposal.command.domain.repository.DisposalRepository;
import com.harusari.chainware.disposal.exception.DisposalErrorCode;
import com.harusari.chainware.disposal.exception.DisposalException;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DisposalCommandServiceImpl implements DisposalCommandService {

    private final DisposalRepository disposalRepository;
    private final FranchiseRepository franchiseRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    @Transactional
    @Override
    public void registerDisposal(DisposalCommandRequestDto request, Long memberId, MemberAuthorityType authorityType) {
        Long franchiseId = null;
        Long warehouseId = null;

        switch (authorityType) {
            case FRANCHISE_MANAGER -> {
                franchiseId = franchiseRepository.findFranchiseIdByMemberId(memberId)
                        .orElseThrow(() -> new DisposalException(DisposalErrorCode.FRANCHISE_NOT_FOUND))
                        .getFranchiseId();
            }
            case WAREHOUSE_MANAGER -> {
                warehouseId = warehouseRepository.findWarehouseIdByMemberId(memberId)
                        .orElseThrow(() -> new DisposalException(DisposalErrorCode.WAREHOUSE_NOT_FOUND))
                        .getWarehouseId();
            }
            default -> throw new DisposalException(DisposalErrorCode.NO_DISPOSAL_AUTHORITY);
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

        if (warehouseId != null && request.getTakeBackId() == null) {
            warehouseInventoryRepository.findByWarehouseIdAndProductId(warehouseId, request.getProductId())
                    .ifPresentOrElse(inventory -> {
                        if (inventory.getQuantity() < request.getQuantity()) {
                            throw new DisposalException(DisposalErrorCode.INSUFFICIENT_INVENTORY);
                        }
                        inventory.decreaseQuantity(request.getQuantity());
                    }, () -> {
                        throw new DisposalException(DisposalErrorCode.INVENTORY_NOT_FOUND);
                    });
        }
    }
}

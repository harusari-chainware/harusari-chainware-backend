package com.harusari.chainware.requisition.command.application.service;

import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.dto.request.RequisitionItemRequest;
import com.harusari.chainware.requisition.command.application.exception.AccessDeniedException;
import com.harusari.chainware.requisition.command.application.exception.InvalidStatusException;
import com.harusari.chainware.requisition.command.application.exception.NotFoundException;
import com.harusari.chainware.requisition.command.domain.aggregate.Requisition;
import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionDetail;
import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import com.harusari.chainware.requisition.command.domain.repository.RequisitionDetailRepository;
import com.harusari.chainware.requisition.command.domain.repository.RequisitionRepository;
import com.harusari.chainware.requisition.command.infrastructure.RequisitionCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequisitionCommandServiceImpl implements RequisitionCommandService {

    private final RequisitionRepository requisitionRepository;
    private final RequisitionDetailRepository requisitionDetailRepository;
    private final RequisitionCodeGenerator requisitionCodeGenerator;

    @Override
    @Transactional
    public Long createRequisition(Long memberId, CreateRequisitionRequest request) {
        List<RequisitionItemRequest> items = request.getItems();

        int productCount = items.size();
        int totalQuantity = items.stream().mapToInt(RequisitionItemRequest::getQuantity).sum();
        BigDecimal totalPrice = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 품의서 코드 생성
        String requisitionCode = requisitionCodeGenerator.generate();

        // Requisition 생성
        Requisition requisition = Requisition.create(
                memberId,
                request.getApprovedMemberId(),
                request.getVendorId(),
                requisitionCode,
                productCount,
                totalQuantity,
                totalPrice
        );

        // RequisitionDetail 리스트 생성
        List<RequisitionDetail> details = items.stream()
                .map(i -> RequisitionDetail.create(
                        i.getContractId(),
                        i.getProductId(),
                        i.getQuantity(),
                        i.getUnitPrice()
                ))
                .toList();

        requisition.addDetails(details); // 연관관계 설정

        // 저장
        requisitionRepository.save(requisition);

        return requisition.getRequisitionId();
    }

    @Override
    @Transactional
    public void submitRequisition(Long memberId, Long requisitionId) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new NotFoundException("해당 품의서를 찾을 수 없습니다."));

        if (!requisition.getCreatedMemberId().equals(memberId)) {
            throw new AccessDeniedException("본인이 작성한 품의서만 상신할 수 있습니다.");
        }

        if (!requisition.getRequisitionStatus().equals(RequisitionStatus.SAVED)) {
            throw new InvalidStatusException("임시 저장된(SAVED) 품의서만 상신할 수 있습니다.");
        }

        requisition.submit(); // 상태 전환 및 submittedAt 갱신
    }
}

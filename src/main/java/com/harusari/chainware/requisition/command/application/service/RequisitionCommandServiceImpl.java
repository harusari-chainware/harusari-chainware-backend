package com.harusari.chainware.requisition.command.application.service;

import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.dto.request.RequisitionItemRequest;
import com.harusari.chainware.requisition.command.application.exception.AccessDeniedException;
import com.harusari.chainware.requisition.command.application.exception.InvalidStatusException;
import com.harusari.chainware.requisition.command.application.exception.NotFoundException;
import com.harusari.chainware.requisition.command.domain.aggregate.RejectRequisitionRequest;
import com.harusari.chainware.requisition.command.domain.aggregate.Requisition;
import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionDetail;
import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import com.harusari.chainware.requisition.command.domain.repository.RequisitionDetailRepository;
import com.harusari.chainware.requisition.command.domain.repository.RequisitionRepository;
import com.harusari.chainware.requisition.command.infrastructure.RequisitionCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
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
        long totalPrice = items.stream()
                .mapToLong(i -> i.getUnitPrice() * i.getQuantity())
                .sum();

        // 품의서 코드 생성
        String requisitionCode = requisitionCodeGenerator.generate();

        // Requisition 직접 생성
        Requisition requisition = Requisition.builder()
                .createdMemberId(memberId)
                .approvedMemberId(request.getApprovedMemberId())
                .vendorId(request.getVendorId())
                .code(requisitionCode)
                .productCount(productCount)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .build();

        // 저장 (먼저 저장해서 ID 생성)
        requisitionRepository.save(requisition);

        Long requisitionId = requisition.getRequisitionId();

        // RequisitionDetail 리스트 생성 및 저장
        List<RequisitionDetail> details = items.stream()
                .map(i -> RequisitionDetail.builder()
                        .requisitionId(requisitionId)
                        .contractId(i.getContractId())
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .build()
                ).toList();

        requisitionDetailRepository.saveAll(details);

        return requisitionId;
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

        requisition.submit();
    }

    @Override
    @Transactional
    public void approveRequisition(Long requisitionId, Long memberId) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new NotFoundException("품의서를 찾을 수 없습니다."));

        if (!requisition.getApprovedMemberId().equals(memberId)) {
            throw new AccessDeniedException("해당 품의서의 결재자가 아닙니다.");
        }

        if (!requisition.getRequisitionStatus().equals(RequisitionStatus.SUBMITTED)) {
            throw new InvalidStatusException("SUBMITTED 상태의 품의서만 승인할 수 있습니다.");
        }

        requisition.approve(memberId);
    }

    @Override
    @Transactional
    public void rejectRequisition(Long requisitionId, Long memberId, RejectRequisitionRequest request) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new NotFoundException("품의서를 찾을 수 없습니다."));

        if (!requisition.getApprovedMemberId().equals(memberId)) {
            throw new AccessDeniedException("해당 품의서의 결재자가 아닙니다.");
        }

        if (!requisition.getRequisitionStatus().equals(RequisitionStatus.SUBMITTED)) {
            throw new InvalidStatusException("SUBMITTED 상태의 품의서만 반려할 수 있습니다.");
        }

        requisition.reject(request.getRejectReason());
    }

    @Override
    @Transactional
    public void deleteRequisition(Long memberId, Long requisitionId) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 품의서를 찾을 수 없습니다."));

        if (!requisition.getCreatedMemberId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        if (!EnumSet.of(RequisitionStatus.SAVED, RequisitionStatus.SUBMITTED)
                .contains(requisition.getRequisitionStatus())) {
            throw new IllegalStateException("삭제 가능한 상태가 아닙니다.");
        }

        requisitionRepository.delete(requisition);
    }
}

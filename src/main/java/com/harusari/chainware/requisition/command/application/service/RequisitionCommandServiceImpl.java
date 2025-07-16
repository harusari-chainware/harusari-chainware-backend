package com.harusari.chainware.requisition.command.application.service;

import com.harusari.chainware.exception.requisition.RequisitionErrorCode;
import com.harusari.chainware.exception.requisition.RequisitionException;
import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.dto.request.RequisitionItemRequest;
import com.harusari.chainware.requisition.command.application.dto.request.UpdateRequisitionRequest;
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


        if (items == null || items.isEmpty()) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_ITEM_NOT_FOUND);
        }

        if (request.getApprovedMemberId() == null) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_APPROVER_REQUIRED);
        }

        if (request.getVendorId() == null) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_VENDOR_REQUIRED);
        }


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
                .warehouseId(request.getWarehouseId())
                .code(requisitionCode)
                .productCount(productCount)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .dueDate(request.getDueDate())
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
    public void updateRequisition(Long requisitionId, UpdateRequisitionRequest request, Long memberId) {
        // 1. 품의서 조회
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND));

        // 2. 권한 확인
        if (!requisition.isWriter(memberId)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_UNAUTHORIZED_WRITER);
        }

        // 3. 수정 가능 상태 확인
        if (!requisition.isModifiable()) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_INVALID_STATUS_UPDATE);
        }

        // 품목 계산
        int productCount = request.getItems().size();
        int totalQuantity = request.getItems().stream()
                .mapToInt(RequisitionItemRequest::getQuantity)
                .sum();
        long totalPrice = request.getItems().stream()
                .mapToLong(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        // 4. requisition 정보 업데이트
        requisition.update(
                request.getApprovedMemberId(),
                request.getWarehouseId(),
                productCount,
                totalQuantity,
                totalPrice
        );

        // 5. 기존 품목 삭제 후 재등록
        requisitionDetailRepository.deleteByRequisitionId(requisitionId);

        List<RequisitionDetail> newDetails = request.getItems().stream()
                .map(item -> RequisitionDetail.builder()
                        .requisitionId(requisition.getRequisitionId())
                        .contractId(item.getContractId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .toList();

        requisitionDetailRepository.saveAll(newDetails);
    }




    @Override
    @Transactional
    public void submitRequisition(Long memberId, Long requisitionId) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND));

        if (!requisition.getCreatedMemberId().equals(memberId)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_UNAUTHORIZED_WRITER);
        }

        if (!requisition.getRequisitionStatus().equals(RequisitionStatus.SAVED)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_INVALID_STATUS_SUBMIT);
        }

        requisition.submit();
    }

    @Override
    @Transactional
    public void approveRequisition(Long requisitionId, Long memberId) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND));

        if (!requisition.getApprovedMemberId().equals(memberId)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_UNAUTHORIZED_APPROVER);
        }

        if (!requisition.getRequisitionStatus().equals(RequisitionStatus.SUBMITTED)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_INVALID_STATUS_APPROVE);
        }

        requisition.approve(memberId);
    }

    @Override
    @Transactional
    public void rejectRequisition(Long memberId, Long requisitionId, RejectRequisitionRequest request) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND));

        if (!requisition.getApprovedMemberId().equals(memberId)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_UNAUTHORIZED_APPROVER);
        }

        if (!requisition.getRequisitionStatus().equals(RequisitionStatus.SUBMITTED)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_INVALID_STATUS_REJECT);
        }

        requisition.reject(request.getRejectReason());
    }

    @Override
    @Transactional
    public void deleteRequisition(Long memberId, Long requisitionId) {
        Requisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND));

        if (!requisition.getCreatedMemberId().equals(memberId)) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_UNAUTHORIZED_WRITER);
        }

        if (!EnumSet.of(RequisitionStatus.SAVED, RequisitionStatus.SUBMITTED)
                .contains(requisition.getRequisitionStatus())) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_INVALID_STATUS_DELETE);
        }
        // 자식 먼저 삭제
        requisitionDetailRepository.deleteByRequisitionId(requisitionId);

        // 부모 삭제
        requisitionRepository.delete(requisition);
    }
}

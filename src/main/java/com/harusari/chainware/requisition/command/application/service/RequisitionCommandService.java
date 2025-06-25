package com.harusari.chainware.requisition.command.application.service;

import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.dto.request.UpdateRequisitionRequest;
import com.harusari.chainware.requisition.command.domain.aggregate.RejectRequisitionRequest;

public interface RequisitionCommandService {

    // 품의서 등록
    Long createRequisition(Long memberId, CreateRequisitionRequest request);

    // 품의서 상신
    void submitRequisition(Long memberId, Long requisitionId);


    void approveRequisition(Long requisitionId, Long memberId);


    void rejectRequisition(Long memberId, Long requisitionId, RejectRequisitionRequest request);

    void deleteRequisition(Long memberId, Long requisitionId);


    void updateRequisition(Long requisitionId, UpdateRequisitionRequest request, Long memberId);

}

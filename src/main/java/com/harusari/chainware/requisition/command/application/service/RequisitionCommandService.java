package com.harusari.chainware.requisition.command.application.service;

import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;

public interface RequisitionCommandService {
    Long createRequisition(Long memberId, CreateRequisitionRequest request);

    void submitRequisition(Long memberId, Long requisitionId);

}

package com.harusari.chainware.requisition.command.domain.repository;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequisitionDetailRepository extends JpaRepository<RequisitionDetail, Long> {
    void deleteByRequisitionId(Long requisitionId);
}

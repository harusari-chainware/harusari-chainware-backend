package com.harusari.chainware.purchase.command.domain.repository;

import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {
    void deleteByPurchaseOrderId(Long requisitionId);

}
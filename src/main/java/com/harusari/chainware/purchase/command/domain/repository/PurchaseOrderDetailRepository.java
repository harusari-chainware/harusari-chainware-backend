package com.harusari.chainware.purchase.command.domain.repository;

import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {
    void deleteByPurchaseOrderId(Long requisitionId);

    List<PurchaseOrderDetail> findByPurchaseOrderId(Long purchaseOrderId);

}
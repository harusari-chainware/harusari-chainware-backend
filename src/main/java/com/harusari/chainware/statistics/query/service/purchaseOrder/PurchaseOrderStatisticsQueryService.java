package com.harusari.chainware.statistics.query.service.purchaseOrder;

import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponseBase;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseOrderStatisticsQueryService {

    List<? extends PurchaseOrderStatisticsResponseBase> getStatistics(
            String period, Long vendorId, LocalDate targetDate, boolean includeProduct);

}
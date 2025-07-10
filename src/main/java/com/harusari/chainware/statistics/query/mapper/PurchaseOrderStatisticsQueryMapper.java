package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderProductStatisticsResponse;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponse;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderTrendResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PurchaseOrderStatisticsQueryMapper {
    List<PurchaseOrderStatisticsResponse> getVendorLevelStatistics(
            @Param("vendorId") Long vendorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<PurchaseOrderProductStatisticsResponse> getProductLevelStatistics(
            @Param("vendorId") Long vendorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<PurchaseOrderTrendResponse> getPurchaseOrderTrendDaily(
            @Param("vendorId") Long vendorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<PurchaseOrderTrendResponse> getPurchaseOrderTrendWeekly(
            @Param("vendorId") Long vendorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<PurchaseOrderTrendResponse> getPurchaseOrderTrendMonthly(
            @Param("vendorId") Long vendorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
package com.harusari.chainware.purchase.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.exception.purchase.PurchaseOrderErrorCode;
import com.harusari.chainware.exception.purchase.PurchaseOrderException;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderListResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSummaryResponse;
import com.harusari.chainware.purchase.query.mapper.PurchaseOrderQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseOrderQueryServiceImpl implements PurchaseOrderQueryService {

    private final PurchaseOrderQueryMapper mapper;

    @Override
    public PurchaseOrderListResponse getPurchaseOrders(Long memberId, PurchaseOrderSearchCondition condition) {
        List<PurchaseOrderSummaryResponse> contents = mapper.findPurchaseOrders(memberId, condition);
        int totalCount = mapper.countPurchaseOrders(memberId, condition);

        Pagination pagination = Pagination.of(
                condition.getPage(),
                condition.getSize(),
                totalCount
        );

        return PurchaseOrderListResponse.builder()
                .contents(contents)
                .pagination(pagination)
                .build();
    }

    @Override
    public PurchaseOrderDetailResponse getPurchaseOrderDetail(Long memberId, Long purchaseOrderId) {
        // 1. 상세 정보 조회 (중첩 구조)
        var detail = mapper.findPurchaseOrderById(memberId, purchaseOrderId);
        if (detail == null) {
            throw new PurchaseOrderException(PurchaseOrderErrorCode.PURCHASE_NOT_FOUND);
        }

        // 2. 상품 목록 조회 후 조립
        var products = mapper.findProductsByPurchaseOrderId(memberId, purchaseOrderId);
        detail.setProducts(products);

        return detail;
    }

}
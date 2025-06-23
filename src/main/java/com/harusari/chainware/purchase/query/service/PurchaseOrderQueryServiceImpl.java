package com.harusari.chainware.purchase.query.service;

import com.harusari.chainware.purchase.query.dto.request.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderSummaryResponse;
import com.harusari.chainware.purchase.query.mapper.PurchaseOrderQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderQueryServiceImpl implements PurchaseOrderQueryService {

    private final PurchaseOrderQueryMapper mapper;

    @Override
    public List<PurchaseOrderSummaryResponse> getPurchaseOrders(Long memberId, PurchaseOrderSearchCondition condition) {
        return mapper.findPurchaseOrders(memberId, condition);
    }

    @Override
    public PurchaseOrderDetailResponse getPurchaseOrderDetail(Long memberId, Long purchaseOrderId) {
        var order = mapper.findPurchaseOrderById(memberId, purchaseOrderId);
        var products = mapper.findProductsByPurchaseOrderId(memberId, purchaseOrderId);

        PurchaseOrderDetailResponse response = new PurchaseOrderDetailResponse();
        response.setPurchaseOrderId(order.getPurchaseOrderId());
        response.setPurchaseOrderCode(order.getPurchaseOrderCode());
        response.setVendorName(order.getVendorName());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setProducts(products);

        return response;
    }
}
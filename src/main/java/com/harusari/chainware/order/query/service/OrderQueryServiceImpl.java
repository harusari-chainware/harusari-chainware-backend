package com.harusari.chainware.order.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.MyFranchiseResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchDetailResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import com.harusari.chainware.order.query.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    @Override
    public PageResponse<OrderSearchResponse> searchOrders(OrderSearchRequest request, Pageable pageable) {
        return PageResponse.from(orderQueryRepository.searchOrders(request, pageable));
    }

    @Override
    public OrderSearchDetailResponse getOrderDetail(Long orderId) {
        return orderQueryRepository.findOrderDetailById(orderId);
    }

    @Override
    public MyFranchiseResponse getMyFranchiseInfo(Long memberId) {
        return orderQueryRepository.findMyFranchiseInfo(memberId);
    }

}

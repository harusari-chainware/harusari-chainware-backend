package com.harusari.chainware.order.query.service;

import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderException;
import com.harusari.chainware.order.query.dto.response.AvailableWarehouseResponse;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderWarehouseQueryServiceImpl implements OrderWarehouseQueryService{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public List<AvailableWarehouseResponse> findAvailableWarehouses(Long orderId) {
        // 주문 존재 여부 확인
        if (!orderRepository.existsById(orderId)) {
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }

        // 주문 상세 목록 조회
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (details.isEmpty()) {
            return Collections.emptyList();
        }

        // 전체 창고 목록 조회
        List<Warehouse> allWarehouses = warehouseRepository.findAll();

        // 각 창고가 모든 제품 수량을 감당할 수 있는지 확인
        List<AvailableWarehouseResponse> availableWarehouses = new ArrayList<>();
        for (Warehouse warehouse : allWarehouses) {
            boolean canHandleAll = details.stream().allMatch(detail -> {
                Optional<WarehouseInventory> optInventory = warehouseInventoryRepository.findByWarehouseIdAndProductId(
                        warehouse.getWarehouseId(), detail.getProductId());
                return optInventory
                        .map(inv -> inv.getQuantity() - inv.getReservedQuantity() >= detail.getQuantity())
                        .orElse(false);
            });

            if (canHandleAll) {
                availableWarehouses.add(new AvailableWarehouseResponse(
                        warehouse.getWarehouseId(), warehouse.getWarehouseName()));
            }
        }

        return availableWarehouses;
    }

}

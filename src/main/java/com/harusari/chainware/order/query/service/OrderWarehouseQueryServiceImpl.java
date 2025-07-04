package com.harusari.chainware.order.query.service;

import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import com.harusari.chainware.order.command.domain.repository.OrderDetailRepository;
import com.harusari.chainware.order.command.domain.repository.OrderRepository;
import com.harusari.chainware.order.exception.OrderErrorCode;
import com.harusari.chainware.order.exception.OrderException;
import com.harusari.chainware.order.query.dto.response.AvailableWarehouseResponse;
import com.harusari.chainware.order.query.dto.response.RiskyProductInfo;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import com.harusari.chainware.product.command.infrastructure.JpaProductRepository;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderWarehouseQueryServiceImpl implements OrderWarehouseQueryService{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseRepository warehouseRepository;

    private final JpaProductRepository productRepository;

    // 주문 가능 창고 조회
    @Override
    public List<AvailableWarehouseResponse> findAvailableWarehouses(Long orderId) {
        // 1. 주문 존재 여부 확인
        if (!orderRepository.existsById(orderId)) {
            throw new OrderException(OrderErrorCode.ORDER_NOT_FOUND);
        }

        // 2. 주문 상세 목록 조회
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (details.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 주문 상세의 제품 정보를 미리 조회
        List<Product> allProducts = productRepository.findAll();
        Map<Long, Product> productMap = allProducts.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        // 4. 전체 창고 목록 조회 및 각 창고 별 주문 가능 여부 검증
        List<Warehouse> allWarehouses = warehouseRepository.findAll();
        List<AvailableWarehouseResponse> availableWarehouses = new ArrayList<>();
        for (Warehouse warehouse : allWarehouses) {
            List<RiskyProductInfo> riskyProducts = new ArrayList<>();
            boolean canHandleAll = true;

            for (OrderDetail detail : details) {
                Long productId = detail.getProductId();
                int orderedQty = detail.getQuantity();

                // 4-1. 창고의 해당 제품 재고 정보 조회
                Optional<WarehouseInventory> optInventory = warehouseInventoryRepository
                        .findByWarehouseIdAndProductId(warehouse.getWarehouseId(), productId);

                // 4-2. 재고가 확보된 창고만 추출
                if (optInventory.isEmpty()) {
                    canHandleAll = false;
                    break;
                }

                WarehouseInventory inventory = optInventory.get();
                int availableQty = inventory.getQuantity() - inventory.getReservedQuantity();

                if (availableQty < orderedQty) {
                    canHandleAll = false;
                    break;
                }

                // 4-3. 위험 제품 판단: 주문 이후 주문가능재고 < 안전재고 인 경우
                if (availableQty - orderedQty < inventory.getSafetyQuantity()) {
                    Product product = productMap.get(productId);
                    riskyProducts.add(new RiskyProductInfo(
                            productId,
                            product.getProductName(),
                            product.getProductCode(),
                            inventory.getSafetyQuantity(),
                            availableQty,
                            orderedQty
                    ));
                }
            }

            // 5. 주문 가능 창고인 경우 반환하도록 추가
            if (canHandleAll) {
                availableWarehouses.add(new AvailableWarehouseResponse(
                        warehouse.getWarehouseId(),
                        warehouse.getWarehouseName(),
                        riskyProducts
                ));
            }
        }

        return availableWarehouses;
    }

}

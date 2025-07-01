package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseInventorySearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.harusari.chainware.delivery.command.domain.aggregate.QDelivery.delivery;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.purchase.command.domain.aggregate.QPurchaseOrder.purchaseOrder;
import static com.harusari.chainware.vendor.command.domain.aggregate.QVendor.vendor;
import static com.harusari.chainware.category.command.domain.aggregate.QTopCategory.topCategory;
import static com.harusari.chainware.category.command.domain.aggregate.QCategory.category;
import static com.harusari.chainware.product.command.domain.aggregate.QProduct.product;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseInventory.warehouseInventory;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseInbound.warehouseInbound;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseOutbound.warehouseOutbound;
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.order.command.domain.aggregate.QOrderDetail.orderDetail;

@Repository
@RequiredArgsConstructor
public class WarehouseInventoryQueryRepositoryImpl implements WarehouseInventoryQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 보유 재고 목록 조회
    @Override
    public Page<WarehouseInventoryInfo> getWarehouseInventories(WarehouseInventorySearchRequest request, Pageable pageable) {
        List<WarehouseInventoryInfo> contents = queryFactory
                .select(Projections.constructor(WarehouseInventoryInfo.class,
                        warehouseInventory.warehouseInventoryId,
                        warehouse.warehouseId,
                        warehouse.warehouseName,
                        product.productId,
                        product.productCode,
                        product.productName,
                        product.unitQuantity,
                        product.unitSpec,
                        warehouseInventory.quantity,
                        warehouseInventory.reservedQuantity,
                        warehouseInventory.createdAt,
                        warehouseInventory.modifiedAt
                ))
                .from(warehouseInventory)
                .join(warehouse).on(warehouseInventory.warehouseId.eq(warehouse.warehouseId))
                .join(product).on(warehouseInventory.productId.eq(product.productId))
                .join(category).on(product.categoryId.eq(category.categoryId))
                .join(topCategory).on(category.topCategoryId.eq(topCategory.topCategoryId))
                .where(
                        warehouse.isDeleted.isFalse(),
                        nameContains(request.getWarehouseName()),
                        addressContains(request.getWarehouseAddress()),
                        statusEq(request.getWarehouseStatus()),
                        productCodeContains(request.getProductCode()),
                        productNameContains(request.getProductName()),
                        topCategoryEq(request.getTopCategoryId()),
                        categoryEq(request.getCategoryId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(warehouseInventory.count())
                .from(warehouseInventory)
                .join(warehouse).on(warehouseInventory.warehouseId.eq(warehouse.warehouseId))
                .join(product).on(warehouseInventory.productId.eq(product.productId))
                .join(category).on(product.categoryId.eq(category.categoryId))
                .join(topCategory).on(category.topCategoryId.eq(topCategory.topCategoryId))
                .where(
                        warehouse.isDeleted.isFalse(),
                        nameContains(request.getWarehouseName()),
                        addressContains(request.getWarehouseAddress()),
                        statusEq(request.getWarehouseStatus()),
                        productCodeContains(request.getProductCode()),
                        productNameContains(request.getProductName()),
                        topCategoryEq(request.getTopCategoryId()),
                        categoryEq(request.getCategoryId())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, Optional.ofNullable(total).orElse(0L));
    }


    @Override
    public WarehouseInventoryDetailResponse findWarehouseInventoryDetail(Long warehouseInventoryId) {

        // 1. 창고 보유 재고 기본 정보 (창고, 상품, 재고)
        Tuple inventoryTuple = queryFactory
                .select(
                        warehouse.warehouseId,
                        warehouse.warehouseName,
                        warehouse.warehouseAddress,
                        warehouse.warehouseStatus,
                        product.productId,
                        product.productCode,
                        product.productName,
                        product.basePrice,
                        product.storeType,
                        category.categoryId,
                        category.categoryName,
                        topCategory.topCategoryId,
                        topCategory.topCategoryName,
                        warehouseInventory.quantity,
                        warehouseInventory.reservedQuantity
                )
                .from(warehouseInventory)
                .join(product).on(warehouseInventory.productId.eq(product.productId))
                .join(category).on(product.categoryId.eq(category.categoryId))
                .join(topCategory).on(topCategory.topCategoryId.eq(category.topCategoryId))
                .join(warehouse).on(warehouseInventory.warehouseId.eq(warehouse.warehouseId))
                .where(warehouseInventory.warehouseInventoryId.eq(warehouseInventoryId))
                .fetchOne();

        if (inventoryTuple == null) {
            throw new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND);
        }

        WarehouseSimpleInfo warehouseInfo = new WarehouseSimpleInfo(
                inventoryTuple.get(warehouse.warehouseId),
                inventoryTuple.get(warehouse.warehouseName),
                inventoryTuple.get(warehouse.warehouseAddress),
                inventoryTuple.get(warehouse.warehouseStatus)
        );

        ProductSimpleInfo productInfo = new ProductSimpleInfo(
                inventoryTuple.get(product.productId),
                inventoryTuple.get(product.productCode),
                inventoryTuple.get(product.productName),
                inventoryTuple.get(topCategory.topCategoryName),
                inventoryTuple.get(category.categoryName),
                inventoryTuple.get(product.basePrice),
                inventoryTuple.get(product.storeType)
        );

        InventorySimpleInfo inventoryInfo = new InventorySimpleInfo(
                inventoryTuple.get(warehouseInventory.quantity),
                inventoryTuple.get(warehouseInventory.reservedQuantity)
        );

        Long productId = inventoryTuple.get(product.productId);
        Long warehouseId = inventoryTuple.get(warehouse.warehouseId);

        // 2. 입고 이력 (최근 10건)
        List<InboundHistory> inboundHistories = queryFactory
                .select(Projections.constructor(InboundHistory.class,
                        vendor.vendorName,
                        purchaseOrder.purchaseOrderCode,
                        purchaseOrder.purchaseOrderId,
                        warehouseInbound.unitQuantity,
//                        warehouseInbound.expiraionDate,
                        warehouseInbound.inboundedAt
                ))
                .from(warehouseInbound)
                .join(purchaseOrder).on(warehouseInbound.purchaseOrderId.eq(purchaseOrder.purchaseOrderId))
                .join(vendor).on(purchaseOrder.vendorId.eq(vendor.vendorId))
                .where(
                        warehouseInbound.warehouseId.eq(warehouseId),
                        warehouseInbound.productId.eq(productId)
                )
                .orderBy(warehouseInbound.inboundedAt.desc())
                .limit(10)
                .fetch();

        // 3. 배송 이력 (최근 10건)
        List<OutboundHistory> outboundHistories = queryFactory
                .select(Projections.constructor(OutboundHistory.class,
                        delivery.trackingNumber,
                        delivery.carrier,
                        delivery.createdAt,
                        delivery.startedAt,
                        delivery.deliveredAt,
                        delivery.deliveryStatus,
                        franchise.franchiseName
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(orderDetail).on(order.orderId.eq(orderDetail.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .where(
                        delivery.warehouseId.eq(warehouseId),
                        orderDetail.productId.eq(productId)
                )
                .orderBy(delivery.createdAt.desc())
                .limit(10)
                .fetch();

        return new WarehouseInventoryDetailResponse(
                warehouseInfo,
                productInfo,
                inventoryInfo,
                inboundHistories,
                outboundHistories
        );
    }

    private BooleanExpression nameContains(String name) {
        return (name != null && !name.isBlank()) ? warehouse.warehouseName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression addressContains(String address) {
        return (address != null && !address.isBlank()) ? warehouse.warehouseAddress.containsIgnoreCase(address) : null;
    }

    private BooleanExpression statusEq(Boolean status) {
        return (status != null) ? warehouse.warehouseStatus.eq(status) : null;
    }

    private BooleanExpression productCodeContains(String code) {
        return (code != null && !code.isBlank()) ? product.productCode.containsIgnoreCase(code) : null;
    }

    private BooleanExpression productNameContains(String name) {
        return (name != null && !name.isBlank()) ? product.productName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression topCategoryEq(Long id) {
        return (id != null) ? topCategory.topCategoryId.eq(id) : null;
    }

    private BooleanExpression categoryEq(Long id) {
        return (id != null) ? category.categoryId.eq(id) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            PathBuilder<?> entityPath = new PathBuilder<>(WarehouseInventory.class, "warehouseInventory");
            com.querydsl.core.types.Order direction = order.isAscending()
                    ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;
            orders.add(new OrderSpecifier<>(direction, entityPath.getComparable(order.getProperty(), Comparable.class)));
        }

        if (orders.isEmpty()) {
            orders.add(new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, warehouseInventory.modifiedAt));
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

}

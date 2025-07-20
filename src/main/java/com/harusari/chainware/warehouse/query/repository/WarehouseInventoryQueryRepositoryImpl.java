package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseInventorySearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.*;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.harusari.chainware.delivery.command.domain.aggregate.QDelivery.delivery;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.purchase.command.domain.aggregate.QPurchaseOrder.purchaseOrder;
import static com.harusari.chainware.vendor.command.domain.aggregate.QVendor.vendor;
import static com.harusari.chainware.category.command.domain.aggregate.QTopCategory.topCategory;
import static com.harusari.chainware.category.command.domain.aggregate.QCategory.category;
import static com.harusari.chainware.product.command.domain.aggregate.QProduct.product;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseInventory.warehouseInventory;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseInbound.warehouseInbound;
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.order.command.domain.aggregate.QOrderDetail.orderDetail;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseOutbound.warehouseOutbound;


@Repository
@RequiredArgsConstructor
public class WarehouseInventoryQueryRepositoryImpl implements WarehouseInventoryQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 보유 재고 목록 조회
    @Override
    public Page<WarehouseInventoryInfo> getWarehouseInventories(WarehouseInventorySearchRequest request, Pageable pageable) {
        List<WarehouseInventoryInfo> contents = queryFactory
                .select(new QWarehouseInventoryInfo(
                        warehouseInventory.warehouseInventoryId,
                        warehouse.warehouseId,
                        warehouse.warehouseName,
                        product.productId,
                        product.productCode,
                        product.productName,
                        product.unitQuantity,
                        product.unitSpec,
                        warehouseInventory.quantity,
                        warehouseInventory.safetyQuantity,
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
                        warehouseIdEq(request.getWarehouseId()),
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
                        warehouseIdEq(request.getWarehouseId()),          // ✅ 동일하게 추가됨
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

    // 보유 재고 상세 조회
    @Override
    public WarehouseInventoryDetailResponse findWarehouseInventoryDetail(Long warehouseInventoryId) {
        // 1. 창고 보유 재고 기본 정보
        WarehouseSimpleInfo warehouseInfo = queryFactory
                .select(new QWarehouseSimpleInfo(
                        warehouse.warehouseId,
                        warehouse.warehouseName,
                        Projections.constructor(Address.class,
                                warehouse.warehouseAddress.zipcode,
                                warehouse.warehouseAddress.addressRoad,
                                warehouse.warehouseAddress.addressDetail
                        ),
                        warehouse.warehouseStatus,
                        member.phoneNumber
                ))
                .from(warehouseInventory)
                .join(warehouse).on(warehouseInventory.warehouseId.eq(warehouse.warehouseId))
                .join(member).on(warehouse.memberId.eq(member.memberId))
                .where(warehouseInventory.warehouseInventoryId.eq(warehouseInventoryId))
                .fetchOne();

        ProductSimpleInfo productInfo = queryFactory
                .select(new QProductSimpleInfo(
                        product.productId,
                        product.productCode,
                        product.productName,
                        topCategory.topCategoryName,
                        category.categoryName,
                        product.basePrice,
                        product.storeType,
                        product.unitSpec
                ))
                .from(warehouseInventory)
                .join(product).on(warehouseInventory.productId.eq(product.productId))
                .join(category).on(product.categoryId.eq(category.categoryId))
                .join(topCategory).on(category.topCategoryId.eq(topCategory.topCategoryId))
                .where(warehouseInventory.warehouseInventoryId.eq(warehouseInventoryId))
                .fetchOne();

        InventorySimpleInfo inventoryInfo = queryFactory
                .select(new QInventorySimpleInfo(
                        warehouseInventory.quantity,
                        warehouseInventory.reservedQuantity,
                        warehouseInventory.safetyQuantity
                ))
                .from(warehouseInventory)
                .where(warehouseInventory.warehouseInventoryId.eq(warehouseInventoryId))
                .fetchOne();

        if (warehouseInfo == null || productInfo == null || inventoryInfo == null) {
            throw new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND);
        }

        Long productId = productInfo.getProductId();
        Long warehouseId = warehouseInfo.getWarehouseId();

        // 2. 입고 이력
        List<InboundHistory> inboundHistories = queryFactory
                .select(Projections.constructor(InboundHistory.class,
                        vendor.vendorName,
                        purchaseOrder.purchaseOrderCode,
                        purchaseOrder.purchaseOrderId,
                        warehouseInbound.unitQuantity,
                        warehouseInbound.expirationDate,
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

        // 3. 출고 이력 (중복 제거 포함)
        List<OutboundHistory> outboundHistories = queryFactory
                .select(Projections.constructor(OutboundHistory.class,
                        delivery.trackingNumber,
                        delivery.carrier,
                        delivery.createdAt,
                        delivery.startedAt,
                        delivery.deliveredAt,
                        delivery.deliveryStatus,
                        franchise.franchiseName,
                        warehouseOutbound.quantity
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .join(warehouseOutbound).on(warehouseOutbound.deliveryId.eq(delivery.deliveryId))
                .where(
                        delivery.warehouseId.eq(warehouseId),
                        warehouseOutbound.productId.eq(productId)
                )
                .orderBy(delivery.startedAt.desc())
                .limit(20) // 넉넉히 가져온 후 중복 제거
                .fetch();

        // 중복 제거: trackingNumber + startedAt + franchise + quantity 기준
        List<OutboundHistory> distinctOutboundHistories = outboundHistories.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                h -> h.getTrackingNumber() + "_" + h.getStartedAt() + "_" + h.getFranchiseName() + "_" + h.getQuantity(),
                                h -> h,
                                (h1, h2) -> h1
                        ),
                        map -> new ArrayList<>(map.values())
                )).stream()
                .sorted(Comparator.comparing(OutboundHistory::getStartedAt).reversed()) // 출고일 내림차순 정렬
                .collect(Collectors.toList());

        return new WarehouseInventoryDetailResponse(
                warehouseInfo,
                productInfo,
                inventoryInfo,
                inboundHistories,
                distinctOutboundHistories
        );
    }

    private BooleanExpression nameContains(String name) {
        return (name != null && !name.isBlank()) ? warehouse.warehouseName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression addressContains(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return warehouse.warehouseAddress.zipcode.containsIgnoreCase(keyword)
                    .or(warehouse.warehouseAddress.addressRoad.containsIgnoreCase(keyword))
                    .or(warehouse.warehouseAddress.addressDetail.containsIgnoreCase(keyword));
        }
        return null;
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

    private BooleanExpression warehouseIdEq(Long warehouseId) {
        return (warehouseId != null) ? warehouse.warehouseId.eq(warehouseId) : null;
    }

}

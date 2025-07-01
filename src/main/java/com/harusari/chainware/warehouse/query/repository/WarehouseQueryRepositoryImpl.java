package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryMethod;
import com.harusari.chainware.member.command.domain.aggregate.QMember;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.*;
import com.querydsl.core.types.ExpressionUtils;
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
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.purchase.command.domain.aggregate.QPurchaseOrder.purchaseOrder;
import static com.harusari.chainware.requisition.command.domain.aggregate.QRequisition.requisition;
import static com.harusari.chainware.vendor.command.domain.aggregate.QVendor.vendor;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;

@Repository
@RequiredArgsConstructor
public class WarehouseQueryRepositoryImpl implements WarehouseQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WarehouseSearchResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable) {
        List<WarehouseSearchResponse> contents = queryFactory
                .select(new QWarehouseSearchResponse(
                        warehouse.warehouseId,
                        warehouse.warehouseName,
                        warehouse.warehouseAddress,
                        member.name,
                        member.phoneNumber,
                        warehouse.warehouseStatus,
                        warehouse.createdAt
                ))
                .from(warehouse)
                .join(member).on(warehouse.memberId.eq(member.memberId))
                .where(
                        warehouse.isDeleted.isFalse(),
                        nameContains(request.getWarehouseName()),
                        addressContains(request.getWarehouseAddress()),
                        statusEq(request.getWarehouseStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .fetch();


        Long count = queryFactory
                .select(warehouse.count())
                .from(warehouse)
                .join(member).on(warehouse.memberId.eq(member.memberId))
                .where(
                        warehouse.isDeleted.isFalse(),
                        nameContains(request.getWarehouseName()),
                        addressContains(request.getWarehouseAddress()),
                        statusEq(request.getWarehouseStatus())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, Optional.ofNullable(count).orElse(0L));
    }

    @Override
    public WarehouseDetailResponse findWarehouseDetailById(Long warehouseId) {
        // 1. 창고 기본 정보
        WarehouseBasicInfo warehouseInfo = queryFactory
                .select(new QWarehouseBasicInfo(
                        warehouse.warehouseName,
                        warehouse.warehouseAddress,
                        warehouse.warehouseStatus,
                        member.name,
                        member.phoneNumber,
                        warehouse.createdAt,
                        warehouse.modifiedAt
                ))
                .from(warehouse)
                .join(member).on(warehouse.memberId.eq(member.memberId))
                .where(warehouse.warehouseId.eq(warehouseId))
                .fetchOne();

        // 2. 입고 이력 (최신 10건)
        QMember creator = new QMember("creator");
        QMember vendorContact = new QMember("vendorContact");

        List<InboundHistoryInfo> inboundHistory = queryFactory
                .select(Projections.constructor(InboundHistoryInfo.class,
                        vendor.vendorName,
                        creator.name,
                        vendorContact.phoneNumber,
                        purchaseOrder.createdAt,
                        purchaseOrder.purchaseOrderStatus,
                        requisition.requisitionId,
                        requisition.requisitionCode,
                        requisition.productCount,
                        requisition.totalQuantity,
                        requisition.totalPrice
                ))
                .from(purchaseOrder)
                .join(vendor).on(purchaseOrder.vendorId.eq(vendor.vendorId))
                .join(creator).on(purchaseOrder.createdMemberId.eq(creator.memberId))
                .join(vendorContact).on(purchaseOrder.vendorMemberId.eq(vendorContact.memberId))
                .join(requisition).on(purchaseOrder.requisitionId.eq(requisition.requisitionId))
                .where(purchaseOrder.warehouseId.eq(warehouseId))
                .orderBy(purchaseOrder.createdAt.desc())
                .limit(10)
                .fetch();

        // 3. 배송 이력 (주문에 대한 배송만, 최신 10건)
        List<OutboundHistoryInfo> outboundHistory = queryFactory
                .select(Projections.constructor(OutboundHistoryInfo.class,
                        delivery.trackingNumber,
                        delivery.carrier,
                        delivery.createdAt,
                        delivery.startedAt,
                        delivery.deliveredAt,
                        delivery.deliveryStatus,
                        franchise.franchiseName
                ))
                .from(delivery)
                .join(franchise).on(delivery.orderId.eq(franchise.franchiseId))
                .where(delivery.warehouseId.eq(warehouseId),
                        delivery.deliveryMethod.eq(DeliveryMethod.HEADQUARTERS))
                .orderBy(delivery.startedAt.desc())
                .limit(10)
                .fetch();

        return WarehouseDetailResponse.builder()
                .warehouseInfo(warehouseInfo)
                .inboundHistory(inboundHistory)
                .outboundHistory(outboundHistory)
                .build();
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

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            PathBuilder<?> entityPath = new PathBuilder<>(warehouse.getType(), warehouse.getMetadata());

            com.querydsl.core.types.Order direction = order.isAscending()
                    ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;
            orders.add(new OrderSpecifier<>(direction, entityPath.getComparable(order.getProperty(), Comparable.class)));
        }

        if (orders.isEmpty()) {
            orders.add(new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, warehouse.createdAt));
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

}

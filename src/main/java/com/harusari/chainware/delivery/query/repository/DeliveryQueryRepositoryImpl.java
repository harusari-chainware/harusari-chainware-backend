package com.harusari.chainware.delivery.query.repository;

import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.DeliverySearchResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.harusari.chainware.delivery.command.domain.aggregate.QDelivery.delivery;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;

@Repository
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DeliverySearchResponse> searchDeliveries(DeliverySearchRequest request, Pageable pageable) {
        List<DeliverySearchResponse> contents = queryFactory
                .select(Projections.constructor(DeliverySearchResponse.class,
                        delivery.trackingNumber,
//                        warehouse.warehouseName,
                        franchise.franchiseName,
                        delivery.carrier,
                        order.orderCode,
                        delivery.deliveryStatus,
                        delivery.startedAt,
                        delivery.deliveredAt
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
//                .join(warehouse).on(order.warehouseId.eq(warehouse.warehouseId))
                .where(buildConditions(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(delivery.startedAt.desc())
                .fetch();

        Long count = queryFactory
                .select(delivery.count())
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
//                .join(warehouse).on(order.warehouseId.eq(warehouse.warehouseId))
                .where(buildConditions(request))
                .fetchOne();

        return new PageImpl<>(contents, pageable, Optional.ofNullable(count).orElse(0L));
    }

    private BooleanBuilder buildConditions(DeliverySearchRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.getFranchiseName() != null && !request.getFranchiseName().isBlank()) {
            builder.and(franchise.franchiseName.containsIgnoreCase(request.getFranchiseName()));
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            builder.and(delivery.startedAt.between(
                    request.getStartDate().atStartOfDay(),
                    request.getEndDate().atTime(LocalTime.MAX)));
        } else if (request.getStartDate() != null) {
            builder.and(delivery.startedAt.goe(request.getStartDate().atStartOfDay()));
        } else if (request.getEndDate() != null) {
            builder.and(delivery.startedAt.loe(request.getEndDate().atTime(LocalTime.MAX)));
        }

        if (request.getDeliveryStatus() != null) {
            builder.and(delivery.deliveryStatus.eq(request.getDeliveryStatus()));
        }

//        if (request.getWarehouseName() != null && !request.getWarehouseName().isBlank()) {
//            builder.and(warehouse.warehouseName.containsIgnoreCase(request.getWarehouseName()));
//        }
//
//        if (request.getWarehouseAddress() != null && !request.getWarehouseAddress().isBlank()) {
//            builder.and(warehouse.warehouseAddress.containsIgnoreCase(request.getWarehouseAddress()));
//        }

//        if (request.getWarehouseStatus() != null) {
//            builder.and(warehouse.warehouseStatus.eq(request.getWarehouseStatus()));
//        }

        return builder;
    }
}

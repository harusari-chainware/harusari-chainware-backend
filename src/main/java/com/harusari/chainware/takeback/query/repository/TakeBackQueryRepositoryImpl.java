package com.harusari.chainware.takeback.query.repository;

import com.harusari.chainware.delivery.command.domain.aggregate.QDelivery;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.QTakeBackSearchResponse;
import com.harusari.chainware.takeback.query.dto.response.TakeBackSearchResponse;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.harusari.chainware.delivery.command.domain.aggregate.QDelivery.delivery;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.takeback.command.domain.aggregate.QTakeBack.takeBack;
import static com.harusari.chainware.takeback.command.domain.aggregate.QTakeBackDetail.takeBackDetail;
import static com.harusari.chainware.vendor.command.domain.aggregate.QVendor.vendor;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;

@Repository
@RequiredArgsConstructor
public class TakeBackQueryRepositoryImpl implements TakeBackQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TakeBackSearchResponse> searchTakeBackList(TakeBackSearchRequest request, Pageable pageable) {
        List<TakeBackSearchResponse> contents = queryFactory
                .select(new QTakeBackSearchResponse(
                        takeBack.takeBackId,
                        takeBack.takeBackCode,
                        takeBack.takeBackStatus,
                        takeBack.createdAt,
                        takeBack.modifiedAt,
                        warehouse.warehouseName,
                        franchise.franchiseName,
                        member.name,
                        order.orderCode,
                        order.orderId
                ))
                .from(takeBack)
                .distinct()
                .join(order).on(takeBack.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .leftJoin(delivery).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .leftJoin(warehouse).on(delivery.warehouseId.eq(warehouse.warehouseId))
                .join(member).on(franchise.memberId.eq(member.memberId))
                .where(
                        warehouseNameContains(request.getWarehouseName()),
                        warehouseAddressContains(request.getWarehouseAddress()),
                        franchiseNameContains(request.getFranchiseName()),
                        takeBackStatusEq(request.getTakeBackStatus()),
                        createdAtBetween(request.getFromDate(), request.getToDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(takeBack.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(takeBack.count())
                .from(takeBack)
                .distinct()
                .join(order).on(takeBack.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .leftJoin(delivery).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .leftJoin(warehouse).on(delivery.warehouseId.eq(warehouse.warehouseId))
                .join(member).on(franchise.memberId.eq(member.memberId))
                .where(
                        warehouseNameContains(request.getWarehouseName()),
                        warehouseAddressContains(request.getWarehouseAddress()),
                        franchiseNameContains(request.getFranchiseName()),
                        takeBackStatusEq(request.getTakeBackStatus()),
                        createdAtBetween(request.getFromDate(), request.getToDate())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, Optional.ofNullable(total).orElse(0L));
    }

    private BooleanExpression warehouseNameContains(String name) {
        return (name != null && !name.isBlank()) ? warehouse.warehouseName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression warehouseAddressContains(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return warehouse.warehouseAddress.zipcode.containsIgnoreCase(keyword)
                    .or(warehouse.warehouseAddress.addressRoad.containsIgnoreCase(keyword))
                    .or(warehouse.warehouseAddress.addressDetail.containsIgnoreCase(keyword));
        }
        return null;
    }

    private BooleanExpression franchiseNameContains(String name) {
        return (name != null && !name.isBlank()) ? franchise.franchiseName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression takeBackStatusEq(TakeBackStatus status) {
        return status != null ? takeBack.takeBackStatus.eq(status) : null;
    }

    private BooleanExpression createdAtBetween(LocalDate from, LocalDate to) {
        if (from != null && to != null) return takeBack.createdAt.between(from.atStartOfDay(), to.atStartOfDay());
        else if (from != null) return takeBack.createdAt.goe(from.atStartOfDay());
        else if (to != null) return takeBack.createdAt.loe(to.atStartOfDay());
        return null;
    }

}

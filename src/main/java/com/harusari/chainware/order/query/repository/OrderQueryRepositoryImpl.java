package com.harusari.chainware.order.query.repository;

import com.harusari.chainware.delivery.command.domain.aggregate.QDelivery;
import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import com.harusari.chainware.franchise.command.domain.aggregate.QFranchise;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import com.harusari.chainware.order.command.domain.aggregate.QOrder;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QOrder order = QOrder.order;
    private final QFranchise franchise = QFranchise.franchise;
    private final QDelivery delivery = QDelivery.delivery;

    @Override
    public Page<OrderSearchResponse> searchOrders(OrderSearchRequest request, Pageable pageable) {
        List<OrderSearchResponse> contents = queryFactory
                .select(Projections.constructor(OrderSearchResponse.class,
                        order.orderCode,
                        franchise.franchiseName,
                        order.productCount,
                        order.totalPrice,
                        delivery.trackingNumber,
                        order.createdAt,
                        order.deliveryDueDate,
                        delivery.deliveredAt,
                        order.orderStatus
                ))
                .from(order)
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .leftJoin(delivery).on(delivery.orderId.eq(order.orderId))
                .where(
                        franchiseNameContains(request.franchiseName()),
                        contractStatusEq(request.contractStatus()),
                        orderStatusEq(request.orderStatus()),
                        createdAtBetween(request.startDate(), request.endDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.createdAt.desc())
                .fetch();

        Long count = queryFactory
                .select(order.count())
                .from(order)
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .where(
                        franchiseNameContains(request.franchiseName()),
                        contractStatusEq(request.contractStatus()),
                        orderStatusEq(request.orderStatus()),
                        createdAtBetween(request.startDate(), request.endDate())
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, Optional.ofNullable(count).orElse(0L));
    }

    private BooleanExpression franchiseNameContains(String name) {
        return (name != null && !name.isBlank()) ? franchise.franchiseName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression contractStatusEq(FranchiseStatus status) {
        return status != null ? franchise.franchiseStatus.eq(status) : null;
    }

    private BooleanExpression orderStatusEq(OrderStatus status) {
        return status != null ? order.orderStatus.eq(status) : null;
    }

    private BooleanExpression createdAtBetween(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return order.createdAt.between(from.atStartOfDay(), to.atTime(LocalTime.MAX));
        } else if (from != null) {
            return order.createdAt.goe(from.atStartOfDay());
        } else if (to != null) {
            return order.createdAt.loe(to.atTime(LocalTime.MAX));
        } else {
            return null;
        }
    }
}

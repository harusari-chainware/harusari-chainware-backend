package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.order.command.domain.aggregate.Order;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
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

import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;

@Repository
@RequiredArgsConstructor
public class WarehouseQueryRepositoryImpl implements WarehouseQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WarehouseSearchResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable) {
        List<WarehouseSearchResponse> contents = queryFactory
                .select(Projections.constructor(WarehouseSearchResponse.class,
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

    private BooleanExpression nameContains(String name) {
        return (name != null && !name.isBlank()) ? warehouse.warehouseName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression addressContains(String address) {
        return (address != null && !address.isBlank()) ? warehouse.warehouseAddress.containsIgnoreCase(address) : null;
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

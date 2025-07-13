package com.harusari.chainware.order.query.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.*;
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

import static com.harusari.chainware.delivery.command.domain.aggregate.QDelivery.delivery;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.order.command.domain.aggregate.QOrderDetail.orderDetail;
import static com.harusari.chainware.product.command.domain.aggregate.QProduct.product;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderSearchResponse> searchOrders(OrderSearchRequest request, Pageable pageable) {
        List<OrderSearchResponse> contents = queryFactory
                .select(Projections.constructor(OrderSearchResponse.class,
                        order.orderId,
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

    @Override
    public OrderSearchDetailResponse findOrderDetailById(Long orderId) {
        OrderBasicInfo basicInfo = queryFactory
                .select(Projections.constructor(OrderBasicInfo.class,
                        order.orderCode,
                        order.orderStatus,
                        delivery.trackingNumber,
                        order.productCount,
                        order.totalPrice,
                        order.deliveryDueDate,
                        order.createdAt,
                        order.modifiedAt
                ))
                .from(order)
                .leftJoin(delivery).on(delivery.orderId.eq(order.orderId))
                .where(order.orderId.eq(orderId))
                .fetchOne();

        FranchiseOwnerInfo franchiseOwnerInfo = queryFactory
                .select(Projections.constructor(FranchiseOwnerInfo.class,
                        member.name,
                        member.phoneNumber,
                        franchise.franchiseName,
                        franchise.franchiseContact,
                        franchise.franchiseTaxId
                ))
                .from(order)
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .join(member).on(franchise.memberId.eq(member.memberId))
                .where(order.orderId.eq(orderId))
                .fetchOne();

        String rejectReason = queryFactory
                .select(order.rejectReason)
                .from(order)
                .where(order.orderId.eq(orderId))
                .fetchOne();

        List<OrderProductInfo> products = queryFactory
                .select(Projections.constructor(OrderProductInfo.class,
                        product.productCode,
                        product.productName,
                        product.unitQuantity,
                        product.unitSpec,
                        product.storeType.stringValue(),
                        orderDetail.unitPrice,
                        orderDetail.quantity,
                        orderDetail.totalPrice
                ))
                .from(orderDetail)
                .join(product).on(orderDetail.productId.eq(product.productId))
                .where(orderDetail.orderId.eq(orderId))
                .fetch();

        List<DeliveryHistoryInfo> deliveryHistory = queryFactory
                .select(Projections.constructor(DeliveryHistoryInfo.class,
                        delivery.startedAt,
                        delivery.deliveredAt,
                        delivery.carrier,
                        delivery.deliveryStatus
                ))
                .from(delivery)
                .where(delivery.orderId.eq(orderId))
                .fetch();

        return OrderSearchDetailResponse.builder()
                .orderInfo(basicInfo)
                .franchiseOwnerInfo(franchiseOwnerInfo)
                .rejectReason(rejectReason)
                .products(products)
                .deliveryHistory(deliveryHistory)
                .build();
    }

    @Override
    public MyFranchiseResponse findMyFranchiseInfo(Long memberId) {
        return queryFactory
                .select(Projections.constructor(MyFranchiseResponse.class,
                        franchise.franchiseName,
                        franchise.franchiseContact,
                        member.name,
                        member.phoneNumber
                ))
                .from(franchise)
                .join(member).on(franchise.memberId.eq(member.memberId))
                .where(member.memberId.eq(memberId))
                .fetchOne();
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

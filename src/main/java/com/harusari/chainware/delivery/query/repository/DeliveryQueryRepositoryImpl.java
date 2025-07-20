package com.harusari.chainware.delivery.query.repository;

import com.harusari.chainware.delivery.query.dto.request.DeliverySearchRequest;
import com.harusari.chainware.delivery.query.dto.response.*;
import com.harusari.chainware.takeback.query.dto.response.QTakeBackProductInfo;
import com.harusari.chainware.takeback.query.dto.response.TakeBackProductInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.order.command.domain.aggregate.QOrderDetail.orderDetail;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.product.command.domain.aggregate.QProduct.product;
import static com.harusari.chainware.takeback.command.domain.aggregate.QTakeBack.takeBack;
import static com.harusari.chainware.takeback.command.domain.aggregate.QTakeBackDetail.takeBackDetail;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;

@Repository
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DeliverySearchResponse> searchDeliveries(DeliverySearchRequest request, Pageable pageable) {
        List<DeliverySearchResponse> contents = queryFactory
                .select(Projections.constructor(DeliverySearchResponse.class,
                        delivery.deliveryId,
                        delivery.orderId,
                        delivery.takeBackId,
                        delivery.trackingNumber,
                        warehouse.warehouseName,
                        franchise.franchiseName,
                        delivery.carrier,
                        order.orderCode,
                        delivery.deliveryStatus,
                        delivery.startedAt,
                        delivery.deliveredAt,
                        delivery.createdAt
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .join(warehouse).on(delivery.warehouseId.eq(warehouse.warehouseId))
                .where(buildConditions(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(delivery.createdAt.desc())
                .fetch();

        Long count = queryFactory
                .select(delivery.count())
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .join(warehouse).on(delivery.warehouseId.eq(warehouse.warehouseId))
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

        if (request.getWarehouseName() != null && !request.getWarehouseName().isBlank()) {
            builder.and(warehouse.warehouseName.containsIgnoreCase(request.getWarehouseName()));
        }

        if (request.getWarehouseAddress() != null && !request.getWarehouseAddress().isBlank()) {
            String keyword = request.getWarehouseAddress();
            builder.and(
                    warehouse.warehouseAddress.zipcode.containsIgnoreCase(keyword)
                            .or(warehouse.warehouseAddress.addressRoad.containsIgnoreCase(keyword))
                            .or(warehouse.warehouseAddress.addressDetail.containsIgnoreCase(keyword))
            );
        }


        if (request.getWarehouseStatus() != null) {
            builder.and(warehouse.warehouseStatus.eq(request.getWarehouseStatus()));
        }

        return builder;
    }

    @Override
    public DeliveryDetailResponse findDeliveryDetailById(Long deliveryId) {
        // 1. 배송 정보
        DeliveryDetailInfo deliveryInfo = queryFactory
                .select(Projections.constructor(DeliveryDetailInfo.class,
                        delivery.trackingNumber,
                        delivery.carrier,
                        delivery.deliveryStatus,
                        delivery.deliveryMethod,
                        delivery.createdAt,
                        delivery.startedAt,
                        delivery.deliveredAt
                ))
                .from(delivery)
                .where(delivery.deliveryId.eq(deliveryId))
                .fetchOne();

        // 2. 창고 정보
        WarehouseInfo warehouseInfo = queryFactory
                .select(Projections.constructor(WarehouseInfo.class,
                        warehouse.warehouseName,
                        warehouse.warehouseAddress,
                        member.name
                ))
                .from(delivery)
                .join(warehouse).on(delivery.warehouseId.eq(warehouse.warehouseId))
                .join(member).on(warehouse.memberId.eq(member.memberId))
                .where(delivery.deliveryId.eq(deliveryId))
                .fetchOne();

        // 3. 가맹점 정보
        FranchiseInfo franchiseInfo = queryFactory
                .select(Projections.constructor(FranchiseInfo.class,
                        franchise.franchiseName,
                        franchise.franchiseAddress,
                        franchise.franchiseTaxId,
                        franchise.franchiseStatus,
                        member.name,
                        member.phoneNumber,
                        franchise.franchiseContact
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .join(member).on(franchise.memberId.eq(member.memberId))
                .where(delivery.deliveryId.eq(deliveryId))
                .fetchOne();

        // 4. 주문 정보
        DeliveryOrderInfo orderInfo = queryFactory
                .select(Projections.constructor(DeliveryOrderInfo.class,
                        order.orderCode,
                        order.productCount,
                        order.totalQuantity,
                        order.totalPrice,
                        order.createdAt
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .where(delivery.deliveryId.eq(deliveryId))
                .fetchOne();

        // 5. 제품 목록
        List<DeliveryProductInfo> products = queryFactory
                .select(Projections.constructor(DeliveryProductInfo.class,
                        orderDetail.orderDetailId,
                        product.productCode,
                        product.productName,
                        product.unitQuantity,
                        product.unitSpec,
                        product.storeType.stringValue(),
                        orderDetail.unitPrice,
                        orderDetail.quantity,
                        orderDetail.totalPrice
                ))
                .from(delivery)
                .join(order).on(delivery.orderId.eq(order.orderId))
                .join(orderDetail).on(order.orderId.eq(orderDetail.orderId))
                .join(product).on(orderDetail.productId.eq(product.productId))
                .where(delivery.deliveryId.eq(deliveryId))
                .fetch();

        // 6. 반품 요약 정보
        DeliveryTakeBackInfo takeBackInfo = queryFactory
                .select(Projections.constructor(DeliveryTakeBackInfo.class,
                        takeBack.takeBackCode,
                        takeBack.takeBackStatus,
                        Expressions.asNumber(
                                JPAExpressions.select(takeBackDetail.count())
                                        .from(takeBackDetail)
                                        .where(takeBackDetail.takeBackId.eq(takeBack.takeBackId))
                        ).intValue(),
                        Expressions.asNumber(
                                JPAExpressions.select(takeBackDetail.quantity.sum())
                                        .from(takeBackDetail)
                                        .where(takeBackDetail.takeBackId.eq(takeBack.takeBackId))
                        ).intValue(),
                        Expressions.asNumber(
                                JPAExpressions.select(takeBackDetail.price.sum())
                                        .from(takeBackDetail)
                                        .where(takeBackDetail.takeBackId.eq(takeBack.takeBackId))
                        ).intValue()
                ))
                .from(delivery)
                .join(takeBack).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .where(delivery.deliveryId.eq(deliveryId))
                .fetchOne();

        // 7. 반품 상세 제품 목록
        List<DeliveryTakeBackProductInfo> takeBackProducts = queryFactory
                .select(new QDeliveryTakeBackProductInfo(
                        takeBackDetail.takeBackDetailId,
                        product.productCode,
                        product.productName,
                        product.unitQuantity,
                        product.unitSpec,
                        product.storeType.stringValue(),
                        takeBackDetail.quantity,
                        takeBackDetail.price,
                        takeBackDetail.takeBackReason,
                        takeBackDetail.takeBackImage
                ))
                .from(delivery)
                .join(takeBack).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .join(takeBackDetail).on(takeBack.takeBackId.eq(takeBackDetail.takeBackId))
                .join(product).on(takeBackDetail.productId.eq(product.productId))
                .where(delivery.deliveryId.eq(deliveryId))
                .fetch();

        return DeliveryDetailResponse.builder()
                .deliveryInfo(deliveryInfo)
                .warehouseInfo(warehouseInfo)
                .franchiseInfo(franchiseInfo)
                .orderInfo(orderInfo)
                .products(products)
                .takeBackInfo(takeBackInfo)
                .takeBackProducts(takeBackProducts)
                .build();
    }

}

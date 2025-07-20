package com.harusari.chainware.takeback.query.repository;

import com.harusari.chainware.delivery.query.dto.response.FranchiseInfo;
import com.harusari.chainware.delivery.query.dto.response.WarehouseInfo;
import com.harusari.chainware.member.command.domain.aggregate.QMember;
import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import com.harusari.chainware.takeback.query.dto.request.TakeBackSearchRequest;
import com.harusari.chainware.takeback.query.dto.response.*;
import com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.harusari.chainware.delivery.command.domain.aggregate.QDelivery.delivery;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.order.command.domain.aggregate.QOrder.order;
import static com.harusari.chainware.takeback.command.domain.aggregate.QTakeBack.takeBack;
import static com.harusari.chainware.takeback.command.domain.aggregate.QTakeBackDetail.takeBackDetail;
import static com.harusari.chainware.product.command.domain.aggregate.QProduct.product;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouse.warehouse;
import static com.harusari.chainware.warehouse.command.domain.aggregate.QWarehouseOutbound.warehouseOutbound;

@Repository
@RequiredArgsConstructor
public class TakeBackQueryRepositoryImpl implements TakeBackQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 반품 목록 조회
    @Override
    public Page<TakeBackSearchResponse> searchTakeBackList(TakeBackSearchRequest request, Pageable pageable) {
        QWarehouse fallbackWarehouse = new QWarehouse("fallbackWarehouse");

        BooleanExpression warehouseNameCondition = warehouseNameContains(request.getWarehouseName(), fallbackWarehouse);
        BooleanExpression warehouseAddressCondition = warehouseAddressContains(request.getWarehouseAddress());
        BooleanExpression franchiseNameCondition = franchiseNameContains(request.getFranchiseName());
        BooleanExpression takeBackStatusCondition = takeBackStatusEq(request.getTakeBackStatus());
        BooleanExpression createdAtCondition = createdAtBetween(request.getFromDate(), request.getToDate());

        List<TakeBackSearchResponse> contents = queryFactory
                .select(new QTakeBackSearchResponse(
                        takeBack.takeBackId,
                        takeBack.takeBackCode,
                        takeBack.takeBackStatus,
                        takeBack.createdAt,
                        takeBack.modifiedAt,
                        warehouse.warehouseName.coalesce(fallbackWarehouse.warehouseName), // 표시용
                        franchise.franchiseName,
                        member.name,
                        order.orderCode,
                        order.orderId
                ))
                .from(takeBack)
                .distinct()
                .join(order).on(takeBack.orderId.eq(order.orderId))
                .join(franchise).on(order.franchiseId.eq(franchise.franchiseId))
                .join(member).on(franchise.memberId.eq(member.memberId))
                .leftJoin(delivery).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .leftJoin(warehouseOutbound).on(warehouseOutbound.deliveryId.eq(delivery.deliveryId))
                .leftJoin(warehouse).on(warehouseOutbound.warehouseId.eq(warehouse.warehouseId))
                .leftJoin(fallbackWarehouse).on(delivery.warehouseId.eq(fallbackWarehouse.warehouseId))
                .where(
                        warehouseNameCondition,
                        warehouseAddressCondition,
                        franchiseNameCondition,
                        takeBackStatusCondition,
                        createdAtCondition
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
                .join(member).on(franchise.memberId.eq(member.memberId))
                .leftJoin(delivery).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .leftJoin(warehouseOutbound).on(warehouseOutbound.deliveryId.eq(delivery.deliveryId))
                .leftJoin(warehouse).on(warehouseOutbound.warehouseId.eq(warehouse.warehouseId))
                .leftJoin(fallbackWarehouse).on(delivery.warehouseId.eq(fallbackWarehouse.warehouseId))
                .where(
                        warehouseNameCondition,
                        warehouseAddressCondition,
                        franchiseNameCondition,
                        takeBackStatusCondition,
                        createdAtCondition
                )
                .fetchOne();

        return new PageImpl<>(contents, pageable, Optional.ofNullable(total).orElse(0L));
    }

    // 반품 상세 조회
    @Override
    public TakeBackDetailResponse findTakeBackDetailById(Long takeBackId) {
        // 별칭 부여
        QMember franchiseManager = new QMember("franchiseManager");
        QMember warehouseManager = new QMember("warehouseManager");

        Tuple basic = queryFactory
                .select(
                        takeBack.takeBackCode,
                        takeBack.takeBackStatus,
                        takeBack.createdAt,
                        takeBack.modifiedAt,
                        takeBack.rejectReason,

                        franchise.franchiseName,
                        franchise.franchiseAddress,
                        franchise.franchiseContact,
                        franchise.franchiseStatus,
                        franchise.franchiseTaxId,
                        franchiseManager.name,
                        franchiseManager.phoneNumber,

                        warehouse.warehouseName,
                        warehouse.warehouseAddress,
                        warehouseManager.name, // ✅ 창고 담당자 이름 // 이놈!!!!!!!!!!!!!!!!

                        order.orderCode,
                        order.productCount,
                        order.totalQuantity,
                        order.totalPrice
                )
                .from(takeBack)
                .join(order).on(order.orderId.eq(takeBack.orderId))
                .join(franchise).on(franchise.franchiseId.eq(order.franchiseId))
                .join(franchiseManager).on(franchise.memberId.eq(franchiseManager.memberId)) // 가맹점 담당자
                .leftJoin(delivery).on(delivery.takeBackId.eq(takeBack.takeBackId))
                .leftJoin(warehouse).on(warehouse.warehouseId.eq(delivery.warehouseId))
                .leftJoin(warehouseManager).on(warehouse.memberId.eq(warehouseManager.memberId)) // ✅ 창고 담당자
                .where(takeBack.takeBackId.eq(takeBackId))
                .fetchOne();

        List<TakeBackProductInfo> productInfos = queryFactory
                .select(new QTakeBackProductInfo(
                        product.productId,
                        product.productCode,
                        product.productName,
                        product.unitQuantity,
                        product.unitSpec,
                        takeBackDetail.quantity,
                        takeBackDetail.price,
                        takeBackDetail.takeBackReason,
                        takeBackDetail.takeBackImage
                ))
                .from(takeBackDetail)
                .join(product).on(takeBackDetail.productId.eq(product.productId))
                .where(takeBackDetail.takeBackId.eq(takeBackId))
                .fetch();

        TakeBackBasicInfo takeBackInfo = new TakeBackBasicInfo(
                basic.get(takeBack.takeBackCode),
                basic.get(takeBack.takeBackStatus),
                basic.get(takeBack.createdAt),
                basic.get(takeBack.modifiedAt),
                basic.get(takeBack.rejectReason)
        );

        FranchiseInfo franchiseInfo = new FranchiseInfo(
                basic.get(franchise.franchiseName),
                basic.get(franchise.franchiseAddress),
                basic.get(franchise.franchiseContact),
                basic.get(franchise.franchiseStatus),
                basic.get(franchise.franchiseTaxId),
                basic.get(franchiseManager.name),
                basic.get(franchiseManager.phoneNumber)
        );

        WarehouseInfo warehouseInfo = new WarehouseInfo(
                basic.get(warehouse.warehouseName),
                basic.get(warehouse.warehouseAddress),
                basic.get(warehouseManager.name) // ✅ 이제 올바른 창고 담당자
        );

        OrderInfo orderInfo = new OrderInfo(
                basic.get(order.orderCode),
                basic.get(order.productCount),
                basic.get(order.totalQuantity),
                basic.get(order.totalPrice)
        );

        return TakeBackDetailResponse.builder()
                .takeBackInfo(takeBackInfo)
                .franchiseInfo(franchiseInfo)
                .warehouseInfo(warehouseInfo)
                .orderInfo(orderInfo)
                .productInfos(productInfos)
                .build();
    }

    private BooleanExpression warehouseNameContains(String name, QWarehouse fallbackWarehouse) {
        if (name != null && !name.isBlank()) {
            return warehouse.warehouseName.containsIgnoreCase(name)
                    .or(fallbackWarehouse.warehouseName.containsIgnoreCase(name));
        }
        return null;
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

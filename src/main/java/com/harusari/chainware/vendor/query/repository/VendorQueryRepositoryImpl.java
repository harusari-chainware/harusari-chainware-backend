package com.harusari.chainware.vendor.query.repository;

import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorContractInfoResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import com.querydsl.core.types.Projections;
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

import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;
import static com.harusari.chainware.vendor.command.domain.aggregate.QVendor.vendor;

@Repository
@RequiredArgsConstructor
public class VendorQueryRepositoryImpl implements VendorQueryRepositoryCustom {

    private static final long TOTAL_DEFAULT_VALUE = 0L;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<VendorSearchResponse> pageVendors(VendorSearchRequest vendorSearchRequest, Pageable pageable) {
        List<VendorSearchResponse> contents = queryFactory
                .select(Projections.constructor(VendorSearchResponse.class,
                        vendor.vendorId, vendor.vendorName, vendor.vendorAddress,
                        member.name, member.phoneNumber, vendor.vendorType,
                        vendor.vendorStatus, vendor.vendorStartDate, vendor.vendorEndDate
                ))
                .from(vendor)
                .leftJoin(member).on(vendor.memberId.eq(member.memberId))
                .where(
                        vendorNameContains(vendorSearchRequest.vendorName()),
                        addressConditions(vendorSearchRequest),
                        vendorTypeEq(vendorSearchRequest.vendorType()),
                        vendorStatusEq(vendorSearchRequest.vendorStatus()),
                        vendorAgreementDateBetween(
                                vendorSearchRequest.contractStartDate(),
                                vendorSearchRequest.contractEndDate()
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(vendor.vendorEndDate.asc())
                .fetch();

        Long result = queryFactory
                .select(vendor.count())
                .from(vendor)
                .leftJoin(member).on(vendor.memberId.eq(member.memberId))
                .where(
                        vendorNameContains(vendorSearchRequest.vendorName()),
                        addressConditions(vendorSearchRequest),
                        vendorTypeEq(vendorSearchRequest.vendorType()),
                        vendorStatusEq(vendorSearchRequest.vendorStatus()),
                        vendorAgreementDateBetween(
                                vendorSearchRequest.contractStartDate(),
                                vendorSearchRequest.contractEndDate()
                        )
                )
                .fetchOne();

        Long total = Optional.ofNullable(result).orElse(TOTAL_DEFAULT_VALUE);

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Optional<VendorDetailResponse> findVendorDetailByVendorId(Long vendorId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(VendorDetailResponse.class,
                        vendor.vendorId, vendor.vendorName, vendor.vendorAddress, member.memberId,
                        member.name, member.phoneNumber, vendor.vendorType, vendor.vendorTaxId,
                        vendor.vendorMemo, vendor.vendorStatus, vendor.agreementOriginalFileName,
                        vendor.agreementFileSize, vendor.vendorStartDate, vendor.vendorEndDate
                ))
                .from(vendor)
                .leftJoin(member).on(vendor.memberId.eq(member.memberId))
                .where(vendor.vendorId.eq(vendorId))
                .fetchOne()
        );
    }

    @Override
    public Optional<VendorContractInfoResponse> findVendorContractInfoByVendorName(String vendorName) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(VendorContractInfoResponse.class,
                        vendor.vendorId, vendor.vendorType, vendor.vendorTaxId, vendor.vendorStatus
                ))
                .from(vendor)
                .where(vendor.vendorName.eq(vendorName))
                .fetchOne()
        );
    }

    private BooleanExpression vendorNameContains(String vendorName) {
        return (vendorName != null && !vendorName.isBlank()) ? vendor.vendorName.contains(vendorName) : null;
    }

    private BooleanExpression addressConditions(VendorSearchRequest vendorSearchRequest) {
        BooleanExpression condition = null;

        if (vendorSearchRequest.zipcode() != null && !vendorSearchRequest.zipcode().isBlank()) {
            condition = safeAnd(condition, franchise.franchiseAddress.zipcode.eq(vendorSearchRequest.zipcode()));
        }
        if (vendorSearchRequest.addressRoad() != null && !vendorSearchRequest.addressRoad().isBlank()) {
            condition = safeAnd(condition, franchise.franchiseAddress.addressRoad.containsIgnoreCase(vendorSearchRequest.addressRoad()));
        }
        if (vendorSearchRequest.addressDetail() != null && !vendorSearchRequest.addressDetail().isBlank()) {
            condition = safeAnd(condition, franchise.franchiseAddress.addressDetail.containsIgnoreCase(vendorSearchRequest.addressDetail()));
        }

        return condition;
    }

    private BooleanExpression safeAnd(BooleanExpression base, BooleanExpression next) {
        return base == null ? next : base.and(next);
    }

    private BooleanExpression vendorTypeEq(com.harusari.chainware.vendor.command.domain.aggregate.VendorType vendorType) {
        return vendorType != null ? vendor.vendorType.eq(vendorType) : null;
    }

    private BooleanExpression vendorStatusEq(com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus vendorStatus) {
        return vendorStatus != null ? vendor.vendorStatus.eq(vendorStatus) : null;
    }

    private BooleanExpression vendorAgreementDateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return vendor.vendorStartDate.loe(endDate)
                    .and(vendor.vendorEndDate.goe(startDate));
        } else if (startDate != null) {
            return vendor.vendorStartDate.goe(startDate);
        } else if (endDate != null) {
            return vendor.vendorEndDate.loe(endDate);
        }
        return null;
    }

}
package com.harusari.chainware.franchise.query.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
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

@Repository
@RequiredArgsConstructor
public class FranchiseQueryRepositoryImpl implements FranchiseQueryRepositoryCustom {

    private static final long TOTAL_DEFAULT_VALUE = 0L;

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FranchiseSearchResponse> pageFranchises(FranchiseSearchRequest franchiseSearchRequest, Pageable pageable) {
        List<FranchiseSearchResponse> contents = queryFactory
                .select(Projections.constructor(FranchiseSearchResponse.class,
                        franchise.franchiseName, member.name, franchise.franchiseContact, franchise.franchiseAddress,
                        franchise.franchiseStatus, franchise.contractStartDate, franchise.contractEndDate
                ))
                .from(franchise)
                .leftJoin(member).on(franchise.memberId.eq(member.memberId))
                .where(
                        franchiseNameContains(franchiseSearchRequest.franchiseName()),
                        addressConditions(franchiseSearchRequest),
                        statusEq(franchiseSearchRequest.franchiseStatus()),
                        franchiseAgreementDateBetween(
                                franchiseSearchRequest.contractStartDate(),
                                franchiseSearchRequest.contractEndDate()
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(franchise.contractEndDate.asc())
                .fetch();

        Long result = queryFactory
                .select(franchise.count())
                .from(franchise)
                .leftJoin(member).on(franchise.memberId.eq(member.memberId))
                .where(
                        franchiseNameContains(franchiseSearchRequest.franchiseName()),
                        addressConditions(franchiseSearchRequest),
                        statusEq(franchiseSearchRequest.franchiseStatus()),
                        franchiseAgreementDateBetween(
                                franchiseSearchRequest.contractStartDate(),
                                franchiseSearchRequest.contractEndDate()
                        )
                )
                .fetchOne();

        long total = Optional.ofNullable(result).orElse(TOTAL_DEFAULT_VALUE);

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Optional<FranchiseSearchDetailResponse> findFranchiseDetailById(Long franchiseId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(FranchiseSearchDetailResponse.class,
                        member.memberId, member.name, member.phoneNumber, franchise.franchiseId,
                        franchise.franchiseName, franchise.franchiseContact, franchise.franchiseTaxId,
                        franchise.franchiseAddress, franchise.agreementOriginalFileName, franchise.agreementFileSize,
                        franchise.contractStartDate, franchise.contractEndDate, franchise.franchiseStatus
                ))
                .from(franchise)
                .leftJoin(member).on(franchise.memberId.eq(member.memberId))
                .where(franchise.franchiseId.eq(franchiseId))
                .fetchOne()
        );
    }

    private BooleanExpression franchiseNameContains(String franchiseName) {
        return franchiseName != null && !franchiseName.isBlank()
                ? franchise.franchiseName.contains(franchiseName)
                : null;
    }

    private BooleanExpression addressConditions(FranchiseSearchRequest franchiseSearchRequest) {
        BooleanExpression condition = null;

        if (franchiseSearchRequest.zipcode() != null && !franchiseSearchRequest.zipcode().isBlank()) {
            condition = safeAnd(condition, franchise.franchiseAddress.zipcode.eq(franchiseSearchRequest.zipcode()));
        }
        if (franchiseSearchRequest.addressRoad() != null && !franchiseSearchRequest.addressRoad().isBlank()) {
            condition = safeAnd(condition, franchise.franchiseAddress.addressRoad.containsIgnoreCase(franchiseSearchRequest.addressRoad()));
        }
        if (franchiseSearchRequest.addressDetail() != null && !franchiseSearchRequest.addressDetail().isBlank()) {
            condition = safeAnd(condition, franchise.franchiseAddress.addressDetail.containsIgnoreCase(franchiseSearchRequest.addressDetail()));
        }

        return condition;
    }

    private BooleanExpression safeAnd(BooleanExpression base, BooleanExpression next) {
        return base == null ? next : base.and(next);
    }

    private BooleanExpression statusEq(FranchiseStatus status) {
        return status != null ? franchise.franchiseStatus.eq(status) : null;
    }

    private BooleanExpression franchiseAgreementDateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return franchise.contractStartDate.loe(endDate)
                    .and(franchise.contractEndDate.goe(startDate));
        } else if (startDate != null) {
            return franchise.contractStartDate.goe(startDate);
        } else if (endDate != null) {
            return franchise.contractEndDate.loe(endDate);
        }
        return null;
    }

}
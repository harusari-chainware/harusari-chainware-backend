package com.harusari.chainware.member.query.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.member.query.dto.request.MemberSearchRequest;
import com.harusari.chainware.member.query.dto.response.MemberSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static com.harusari.chainware.member.command.domain.aggregate.QAuthority.authority;
import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByEmail(String email) {
        return queryFactory
                .selectOne()
                .from(member)
                .where(member.email.eq(email))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Member> findActiveMemberByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(member)
                        .where(
                                member.email.eq(email),
                                member.isDeleted.eq(false)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Page<MemberSearchResponse> findMembers(MemberSearchRequest memberSearchRequest, Pageable pageable) {
        List<MemberSearchResponse> contents = queryFactory
                .select(Projections.constructor(MemberSearchResponse.class,
                        member.email, member.name, authority.authorityLabelKr,
                        member.phoneNumber, member.birthDate, member.position,
                        member.joinAt, member.isDeleted
                ))
                .from(member)
                .leftJoin(authority).on(member.authorityId.eq(authority.authorityId))
                .where(
                        emailEq(memberSearchRequest.email()),
                        authorityEq(memberSearchRequest.authorityName()),
                        joinDateBetween(memberSearchRequest.joinDateFrom(), memberSearchRequest.joinDateTo()),
                        isDeletedFalse(memberSearchRequest.isDeleted())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.memberId.asc())
                .fetch();

        Long result = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(authority).on(member.authorityId.eq(authority.authorityId))
                .where(
                        emailEq(memberSearchRequest.email()),
                        authorityEq(memberSearchRequest.authorityName()),
                        joinDateBetween(memberSearchRequest.joinDateFrom(), memberSearchRequest.joinDateTo())
                )
                .fetchOne();

        final long TOTAL_DEFAULT_VALUE = 0L;
        long total = Optional.ofNullable(result).orElse(TOTAL_DEFAULT_VALUE);

        return new PageImpl<>(contents, pageable, total);
    }

    private BooleanExpression emailEq(String email) {
        return email != null ? member.email.eq(email) : null;
    }

    private BooleanExpression authorityEq(MemberAuthorityType authorityName) {
        return authorityName != null ? authority.authorityName.eq(authorityName) : null;
    }

    private BooleanExpression joinDateBetween(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return member.joinAt.between(from.atStartOfDay(), to.atTime(LocalTime.MAX));
        } else if (from != null) {
            return member.joinAt.goe(from.atStartOfDay());
        } else if (to != null) {
            return member.joinAt.loe(to.atTime(LocalTime.MAX));
        } else {
            return null;
        }
    }

    private BooleanExpression isDeletedFalse(boolean isDeleted) {
        return !isDeleted ? member.isDeleted.eq(false) : member.isDeleted.eq(true);
    }

}
package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.harusari.chainware.member.command.domain.aggregate.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCommandRepositoryCustomImpl implements MemberCommandRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findActiveMemberByEmail(String email) {
        return queryFactory
                .selectFrom(member)
                .where(
                        member.email.eq(email),
                        member.isDeleted.eq(false)
                )
                .fetchOne();
    }

}
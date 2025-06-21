package com.harusari.chainware.member.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                .where(
                        member.email.eq(email),
                        member.isDeleted.eq(false)
                )
                .fetchFirst() != null;
    }

}
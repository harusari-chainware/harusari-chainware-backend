package com.harusari.chainware.franchise.command.domain.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.harusari.chainware.franchise.command.domain.aggregate.FranchiseStatus.OPERATING;
import static com.harusari.chainware.franchise.command.domain.aggregate.QFranchise.franchise;

@Repository
@RequiredArgsConstructor
public class FranchiseCommandRepositoryImpl implements FranchiseCommandRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Franchise> findFranchiseByFranchiseId(Long franchiseId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(franchise)
                        .where(
                                franchise.franchiseId.eq(franchiseId),
                                franchise.franchiseStatus.eq(OPERATING)
                        )
                        .fetchOne()
        );
    }

}
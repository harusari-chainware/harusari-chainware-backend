package com.harusari.chainware.franchise.command.domain.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseRepository extends FranchiseCommandRepositoryCustom, JpaRepository<Franchise, Long> {

    Optional<Franchise> findFranchiseIdByMemberId(Long memberId);

}
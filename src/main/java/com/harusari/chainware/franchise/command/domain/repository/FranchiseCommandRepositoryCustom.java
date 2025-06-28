package com.harusari.chainware.franchise.command.domain.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;

import java.util.Optional;

public interface FranchiseCommandRepositoryCustom {

    Optional<Franchise> findFranchiseByFranchiseId(Long franchiseId);

}
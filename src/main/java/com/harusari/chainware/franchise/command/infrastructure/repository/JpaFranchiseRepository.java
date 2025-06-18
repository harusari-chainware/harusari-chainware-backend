package com.harusari.chainware.franchise.command.infrastructure.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.franchise.command.domain.repository.FranchiseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFranchiseRepository extends FranchiseRepository, JpaRepository<Franchise, Long> {

}
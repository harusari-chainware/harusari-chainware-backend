package com.harusari.chainware.franchise.command.domain.repository;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;

public interface FranchiseRepository {

    Franchise save(Franchise franchise);

}
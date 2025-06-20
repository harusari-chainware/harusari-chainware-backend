package com.harusari.chainware.contract.command.infrastructure;

import com.harusari.chainware.contract.command.domain.aggregate.Contract;
import com.harusari.chainware.contract.command.domain.repository.ContractRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaContractRepository extends ContractRepository, JpaRepository<Contract, Long> {

}
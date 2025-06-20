package com.harusari.chainware.contract.command.domain.repository;

import com.harusari.chainware.contract.command.domain.aggregate.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {

    Optional<Contract> findById(Long contractId);

    List<Contract> findByVendorIdAndIsDeletedFalse(Long vendorId);

    List<Contract> findByProductIdAndIsDeletedFalse(Long productId);

    boolean existsByProductIdAndVendorIdAndIsDeletedFalse(Long productId, Long vendorId);

    Contract save(Contract contract);
}
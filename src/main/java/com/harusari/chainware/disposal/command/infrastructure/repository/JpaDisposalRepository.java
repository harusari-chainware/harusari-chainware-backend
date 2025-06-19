package com.harusari.chainware.disposal.command.infrastructure.repository;

import com.harusari.chainware.disposal.command.domain.aggregate.Disposal;
import com.harusari.chainware.disposal.command.domain.repository.DisposalRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDisposalRepository extends DisposalRepository, JpaRepository<Disposal, Long> {
}

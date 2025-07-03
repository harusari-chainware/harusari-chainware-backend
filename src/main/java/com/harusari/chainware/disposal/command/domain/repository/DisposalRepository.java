package com.harusari.chainware.disposal.command.domain.repository;

import com.harusari.chainware.disposal.command.domain.aggregate.Disposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisposalRepository extends JpaRepository<Disposal, Long> {
}

package com.harusari.chainware.requisition.command.domain.repository;

import com.harusari.chainware.requisition.command.domain.aggregate.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

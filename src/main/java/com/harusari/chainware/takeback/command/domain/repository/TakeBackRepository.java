package com.harusari.chainware.takeback.command.domain.repository;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TakeBackRepository extends JpaRepository<TakeBack, Long> {
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}

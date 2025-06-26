package com.harusari.chainware.takeback.command.domain.repository;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeBackDetailRepository extends JpaRepository<TakeBackDetail, Long> {
}

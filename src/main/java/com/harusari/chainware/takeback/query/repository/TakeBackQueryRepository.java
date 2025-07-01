package com.harusari.chainware.takeback.query.repository;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeBackQueryRepository extends JpaRepository<TakeBack, Long>, TakeBackQueryRepositoryCustom {
}

package com.harusari.chainware.category.command.infrastructure;

import com.harusari.chainware.category.command.domain.aggregate.TopCategory;
import com.harusari.chainware.category.command.domain.repository.TopCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTopCategoryRepository extends TopCategoryRepository, JpaRepository<TopCategory, Long> {
}
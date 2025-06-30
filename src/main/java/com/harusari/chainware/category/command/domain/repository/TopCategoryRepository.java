package com.harusari.chainware.category.command.domain.repository;

import com.harusari.chainware.category.command.domain.aggregate.TopCategory;

import java.util.Optional;

public interface TopCategoryRepository {
    Optional<TopCategory> findByTopCategoryId(Long topCategoryId);
}
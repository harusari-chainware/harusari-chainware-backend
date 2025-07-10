package com.harusari.chainware.category.command.domain.repository;

import com.harusari.chainware.category.command.domain.aggregate.TopCategory;

import java.util.List;
import java.util.Optional;

public interface TopCategoryRepository {
    Optional<TopCategory> findByTopCategoryId(Long topCategoryId);

    boolean existsByTopCategoryName(String topCategoryName);

    boolean existsByTopCategoryNameAndTopCategoryIdNot(String topCategoryName, Long topCategoryId);

    TopCategory save(TopCategory topCategory);

    void delete(TopCategory topCategory);

    boolean existsById(Long topCategoryId);

    List<TopCategory> findAll();
}
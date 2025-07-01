package com.harusari.chainware.category.command.domain.repository;

import com.harusari.chainware.category.command.domain.aggregate.Category;

import java.util.Optional;

public interface CategoryRepository {

    boolean existsByTopCategoryId(Long topCategoryId);

    boolean existsByTopCategoryIdAndCategoryName(Long topCategoryId, String categoryName);

    Category save(Category category);

    Optional<Category> findById(Long categoryId);

    void delete(Category category);
}
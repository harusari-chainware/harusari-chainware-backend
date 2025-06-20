package com.harusari.chainware.category.command.infrastructure;

import com.harusari.chainware.category.command.domain.aggregate.Category;
import com.harusari.chainware.category.command.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, Long> {

    boolean existsByTopCategoryId(Long topCategoryId);

    boolean existsByTopCategoryIdAndCategoryName(Long topCategoryId, String categoryName);

}
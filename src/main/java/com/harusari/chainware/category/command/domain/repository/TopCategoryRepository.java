package com.harusari.chainware.category.command.domain.repository;

import com.harusari.chainware.category.command.domain.aggregate.TopCategory;

import java.util.Optional;

public interface TopCategoryRepository {

    TopCategory save(TopCategory topCategory);

    void delete(TopCategory topCategory);

    Optional<TopCategory> findByTopCategoryId(Long topCategoryId);

}
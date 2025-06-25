package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.response.CategoryDetailResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryDetailResponse;
import com.harusari.chainware.category.query.dto.response.TopCategoryResponse;

import java.util.List;

public interface CategoryQueryService {
    List<TopCategoryResponse> getCategoryListWithProductCount();

    TopCategoryDetailResponse getTopCategoryDetail(Long topCategoryId);

    CategoryDetailResponse getCategoryDetail(Long categoryId);
}
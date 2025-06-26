package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.response.*;

public interface CategoryQueryService {

    // 1. 카테고리 기준 페이징 전체 조회 (상위 카테고리 + 카테고리 + 제품 수)
    TopCategoryListResponse getAllCategoriesGroupedByTopCategory(int page, int size) ;

    // 2. 특정 상위 카테고리 기준 제품 정보 페이징 조회
    TopCategoryProductPageResponse getTopCategoryWithPagedProducts(Long topCategoryId, int page, int size);

    // 3. 특정 카테고리 상세 조회 (상위 카테고리 + 제품 정보 포함)
    CategoryDetailResponse getCategoryDetailWithProducts(Long categoryId, int page, int size);
}
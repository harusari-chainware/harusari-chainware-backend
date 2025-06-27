package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.request.CategoryByTopCategoryRequest;
import com.harusari.chainware.category.query.dto.response.*;

public interface CategoryQueryService {
    TopCategoryListResponse getTopCategoryListWithPaging(int page, int size);

    CategoryListResponse getCategoriesByTopCategory(CategoryByTopCategoryRequest request);

    ProductListWithPagination getCategoryProducts(Long categoryId, int page, int size);
}
package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.request.CategorySearchRequest;
import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.category.query.mapper.CategoryQueryMapper;
import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.exception.category.CategoryErrorCode;
import com.harusari.chainware.exception.category.TopCategoryNotFoundException;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryQueryMapper categoryQueryMapper;

    @Override
    public TopCategoryListResponse searchCategories(CategorySearchRequest request) {
        int offset = request.getOffset();
        int limit = request.getLimit();

        List<CategoryWithTopResponse> flatList =
                categoryQueryMapper.searchCategoriesWithTopAndProductCount(request, offset, limit);
        long total = categoryQueryMapper.countCategoriesWithCondition(request);

        Map<Long, TopCategoryWithCategoriesResponse> grouped = new LinkedHashMap<>();
        for (CategoryWithTopResponse row : flatList) {
            grouped.computeIfAbsent(row.getTopCategoryId(), id ->
                    TopCategoryWithCategoriesResponse.builder()
                            .topCategoryId(row.getTopCategoryId())
                            .topCategoryName(row.getTopCategoryName())
                            .categories(new ArrayList<>())
                            .build()
            ).getCategories().add(
                    CategoryWithProductCountResponse.builder()
                            .categoryId(row.getCategoryId())
                            .categoryName(row.getCategoryName())
                            .productCount(row.getProductCount())
                            .build()
            );
        }

    return TopCategoryListResponse.builder()
            .topCategories(new ArrayList<>(grouped.values()))
            .pagination(Pagination.of(request.getPage(), request.getSize(), total))
            .build();
}

    @Override
    public TopCategoryProductPageResponse getTopCategoryWithPagedProducts(Long topCategoryId, int page, int size) {
        TopCategoryOnlyResponse topCategory = categoryQueryMapper.selectTopCategoryBasic(topCategoryId);
        if (topCategory == null) {
            throw new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
        }

        int offset = (page - 1) * size;
        long total = categoryQueryMapper.countProductsByTopCategoryId(topCategoryId);

        // 페이징된 제품 리스트 (카테고리 정보 포함)
        List<ProductDto> pagedProducts =
                categoryQueryMapper.selectProductsByTopCategoryId(topCategoryId, offset, size);

        // 카테고리 ID 기준으로 그룹핑
        Map<Long, CategoryWithProductsResponse> grouped = new LinkedHashMap<>();
        for (ProductDto product : pagedProducts) {
            grouped.computeIfAbsent(product.getCategoryId(), id -> {
                String categoryName = categoryQueryMapper.selectCategoryNameById(id); // 또는 product에서 가져오되 1회만
                return CategoryWithProductsResponse.builder()
                        .categoryId(id)
                        .categoryName(categoryName) // 여기만 따로 가져옴
                        .products(new ArrayList<>())
                        .build();
            }).getProducts().add(product);
        }

        return TopCategoryProductPageResponse.builder()
                .topCategoryId(topCategory.getTopCategoryId())
                .topCategoryName(topCategory.getTopCategoryName())
                .categories(new ArrayList<>(grouped.values()))
                .pagination(Pagination.of(page, size, total))
                .build();
    }

    @Override
    public CategoryDetailResponse getCategoryDetailWithProducts(Long categoryId, int page, int size) {
        int offset = (page - 1) * size;
        CategoryDetailInfoResponse categoryInfo = categoryQueryMapper.selectCategoryInfoWithTop(categoryId);
        List<ProductDto> products = categoryQueryMapper.selectProductsByCategoryId(categoryId, offset, size);
        long total = categoryQueryMapper.countProductsByCategoryId(categoryId);

        return CategoryDetailResponse.builder()
                .categoryId(categoryInfo.getCategoryId())
                .categoryName(categoryInfo.getCategoryName())
                .topCategoryId(categoryInfo.getTopCategoryId())
                .topCategoryName(categoryInfo.getTopCategoryName())
                .products(products)
                .pagination(Pagination.of(page, size, total))
                .build();
    }
}
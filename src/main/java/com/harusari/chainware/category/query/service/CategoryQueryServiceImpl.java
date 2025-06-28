package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.request.CategorySearchRequest;
import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.category.query.mapper.CategoryQueryMapper;
import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.exception.category.CategoryErrorCode;
import com.harusari.chainware.exception.category.CategoryNotFoundException;
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

        List<ProductDto> pagedProducts = categoryQueryMapper.selectProductsByTopCategoryId(topCategoryId, offset, size);

        Map<Long, CategoryWithProductsResponse> grouped = new LinkedHashMap<>();
        for (ProductDto product : pagedProducts) {
            grouped.computeIfAbsent(product.getCategoryId(), id -> {
                CategoryMetaInfoResponse meta = categoryQueryMapper.selectCategoryBasic(id);
                return CategoryWithProductsResponse.builder()
                        .categoryId(meta.getCategoryId())
                        .categoryName(meta.getCategoryName())
                        .categoryCreatedAt(meta.getCreatedAt())
                        .categoryModifiedAt(meta.getModifiedAt())
                        .products(new ArrayList<>())
                        .build();
            }).getProducts().add(product);
        }

        return TopCategoryProductPageResponse.builder()
                .topCategoryId(topCategory.getTopCategoryId())
                .topCategoryName(topCategory.getTopCategoryName())
                .createdAt(topCategory.getCreatedAt())
                .modifiedAt(topCategory.getModifiedAt())
                .categories(new ArrayList<>(grouped.values()))
                .pagination(Pagination.of(page, size, total))
                .build();
    }

    @Override
    public CategoryDetailWithProductsResponse getCategoryDetailWithProducts(
            Long categoryId, int page, int size) {

        // 1) 기존 DTO로 하위 카테고리 기본 정보 가져오기
        CategoryMetaInfoResponse categoryMeta =
                categoryQueryMapper.selectCategoryBasic(categoryId);
        if (categoryMeta == null) {
            throw new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }

        // 2) 상위 카테고리 기본 정보 가져오기
        Long topCategoryId = categoryQueryMapper.selectTopCategoryIdByCategoryId(categoryId);

        TopCategoryOnlyResponse topCategory =
                categoryQueryMapper.selectTopCategoryBasic(topCategoryId);

        // 3) 상품 전체 수 + 페이징 계산
        int offset = (page - 1) * size;
        long total = categoryQueryMapper.countProductsByCategoryId(categoryId);

        // 4) 페이징된 상품 리스트
        List<ProductDto> products =
                categoryQueryMapper.selectProductsByCategoryId(categoryId, offset, size);

        // 5) 응답 DTO 조립
        return CategoryDetailWithProductsResponse.builder()
                .categoryMeta(categoryMeta)
                .topCategory(topCategory)
                .products(products)
                .pagination(Pagination.of(page, size, total))
                .build();
    }
}
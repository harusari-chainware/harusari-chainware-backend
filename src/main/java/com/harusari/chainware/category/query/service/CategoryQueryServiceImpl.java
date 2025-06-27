package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.request.CategoryByTopCategoryRequest;
import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.category.query.mapper.CategoryQueryMapper;
import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryQueryMapper categoryQueryMapper;

    @Override
    public TopCategoryListResponse getTopCategoryListWithPaging(int page, int size) {
        int offset = (page - 1) * size;
        List<TopCategoryOnlyResponse> topCategories = categoryQueryMapper.selectTopCategoriesWithPaging(offset, size);
        long totalCount = categoryQueryMapper.countAllTopCategories();

        List<TopCategoryResponse> categoryResponses = topCategories.stream()
                .map(top -> new TopCategoryResponse(
                        top.getTopCategoryId(),
                        top.getTopCategoryName(),
                        categoryQueryMapper.selectCategoriesByTopCategoryId(
                                top.getTopCategoryId(),
                                0,
                                Integer.MAX_VALUE // 전체 다 가져오기
                        )
                ))
                .collect(Collectors.toList());

        return TopCategoryListResponse.builder()
                .topCategories(categoryResponses)
                .pagination(Pagination.of(page, size, totalCount))
                .build();
    }

    @Override
    public CategoryListResponse getCategoriesByTopCategory(CategoryByTopCategoryRequest request) {
        List<CategoryResponse> categories = categoryQueryMapper.selectCategoriesByTopCategoryId(
                request.getTopCategoryId(),
                request.getOffset(),
                request.getLimit()
        );

        long totalCount = categoryQueryMapper.countCategoriesByTopCategoryId(request.getTopCategoryId());

        return CategoryListResponse.builder()
                .categories(categories)
                .pagination(Pagination.of(request.getPage(), request.getSize(), totalCount))
                .build();
    }

    @Override
    public ProductListWithPagination getCategoryProducts(Long categoryId, int page, int size) {
        int offset = (page - 1) * size;
        List<ProductDto> products = categoryQueryMapper.selectProductsByCategoryId(categoryId, offset, size);
        long totalCount = categoryQueryMapper.countProductsByCategoryId(categoryId);

        return ProductListWithPagination.builder()
                .products(products)
                .pagination(Pagination.of(page, size, totalCount))
                .build();
    }
}
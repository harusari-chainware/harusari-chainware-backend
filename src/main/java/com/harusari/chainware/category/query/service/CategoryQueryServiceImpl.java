package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.category.query.mapper.CategoryQueryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryQueryMapper categoryQueryMapper;

    public CategoryQueryServiceImpl(CategoryQueryMapper mapper) {
        this.categoryQueryMapper = mapper;
    }

    @Override
    public List<TopCategoryResponse> getCategoryListWithProductCount() {
        return categoryQueryMapper.selectTopCategories().stream()
                .map(top -> new TopCategoryResponse(
                        top.getTopCategoryId(),
                        top.getTopCategoryName(),
                        categoryQueryMapper.selectCategoriesByTopCategoryId(top.getTopCategoryId())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public TopCategoryDetailResponse getTopCategoryDetail(Long topCategoryId) {
        // 상위 카테고리 기본 정보 조회 (ID, 이름)
        TopCategoryOnlyResponse topCategory = categoryQueryMapper.selectTopCategoryBasic(topCategoryId);

        // 해당 상위 카테고리에 포함된 카테고리 + 제품 목록 조회
        List<CategoryWithProductsResponse> categories = categoryQueryMapper.selectCategoriesWithProductsByTopCategoryId(topCategoryId);

        return new TopCategoryDetailResponse(
                topCategory.getTopCategoryId(),
                topCategory.getTopCategoryName(),
                categories
        );
    }

    @Override
    public CategoryDetailResponse getCategoryDetail(Long categoryId) {
        return categoryQueryMapper.selectCategoryDetailWithProducts(categoryId);
    }
}
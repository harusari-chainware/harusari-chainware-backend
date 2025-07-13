package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.command.domain.repository.TopCategoryRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryQueryMapper categoryQueryMapper;
    private final TopCategoryRepository topCategoryRepository;


    @Override
    public TopCategoryListResponse searchCategories(CategorySearchRequest request) {
        int offset = request.getOffset();
        int limit = request.getLimit();

        List<CategoryWithTopResponse> flatList =
                categoryQueryMapper.searchCategoriesWithTopAndProductCount(request, offset, limit);
        long total = categoryQueryMapper.countCategoriesWithCondition(request);

        Map<Long, List<CategoryWithProductCountResponse>> categoryMap = new LinkedHashMap<>();
        Map<Long, String> topCategoryNameMap = new HashMap<>();
        Map<Long, Long> topCategoryProductCountMap = new HashMap<>();

        for (CategoryWithTopResponse row : flatList) {
            categoryMap.computeIfAbsent(row.getTopCategoryId(), k -> new ArrayList<>()).add(
                    CategoryWithProductCountResponse.builder()
                            .categoryId(row.getCategoryId())
                            .categoryName(row.getCategoryName())
                            .categoryCode(row.getCategoryCode())
                            .productCount(row.getProductCount())
                            .createdAt(row.getCreatedAt())
                            .modifiedAt(row.getModifiedAt())
                            .build()
            );

            topCategoryNameMap.putIfAbsent(row.getTopCategoryId(), row.getTopCategoryName());
            topCategoryProductCountMap.put(
                    row.getTopCategoryId(),
                    topCategoryProductCountMap.getOrDefault(row.getTopCategoryId(), 0L) + row.getProductCount()
            );
        }

        List<TopCategoryWithCategoriesResponse> topCategories = categoryMap.entrySet().stream()
                .map(entry -> TopCategoryWithCategoriesResponse.builder()
                        .topCategoryId(entry.getKey())
                        .topCategoryName(topCategoryNameMap.get(entry.getKey()))
                        .productCount(topCategoryProductCountMap.getOrDefault(entry.getKey(), 0L))
                        .categories(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return TopCategoryListResponse.builder()
                .topCategories(topCategories)
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

        // ① 모든 하위 카테고리 먼저 조회 (products 없어도 무조건 포함!)
        List<CategoryMetaInfoResponse> allCategories =
                categoryQueryMapper.selectAllCategoriesByTopCategoryId(topCategoryId);

        // ② 카테고리별 제품수 맵 조회
        Map<Long, Long> categoryProductCountMap = categoryQueryMapper
                .selectCategoryProductCountsByTopCategoryId(topCategoryId)
                .stream()
                .collect(Collectors.toMap(
                        CategoryProductCountResponse::getCategoryId,
                        CategoryProductCountResponse::getProductCount
                ));

        // ③ 페이징에 해당하는 모든 제품 조회
        List<ProductDto> pagedProducts = categoryQueryMapper.selectProductsByTopCategoryId(topCategoryId, offset, size);

        // ④ 하위 카테고리별 응답 DTO 미리 만듦 (products 없는 경우도 포함)
        Map<Long, CategoryWithProductsResponse> grouped = new LinkedHashMap<>();
        for (CategoryMetaInfoResponse meta : allCategories) {
            grouped.put(meta.getCategoryId(),
                    CategoryWithProductsResponse.builder()
                            .categoryId(meta.getCategoryId())
                            .categoryName(meta.getCategoryName())
                            .categoryCreatedAt(meta.getCreatedAt())
                            .categoryModifiedAt(meta.getModifiedAt())
                            .productCount(categoryProductCountMap.getOrDefault(meta.getCategoryId(), 0L))
                            .products(new ArrayList<>())
                            .build());
        }

        // ⑤ 실제 제품이 있으면 해당 카테고리에 추가
        for (ProductDto product : pagedProducts) {
            CategoryWithProductsResponse cat = grouped.get(product.getCategoryId());
            if (cat != null) {
                cat.getProducts().add(product);
            }
        }

        return TopCategoryProductPageResponse.builder()
                .topCategoryId(topCategory.getTopCategoryId())
                .topCategoryName(topCategory.getTopCategoryName())
                .createdAt(topCategory.getCreatedAt())
                .modifiedAt(topCategory.getModifiedAt())
                .productCount(total)
                .categories(new ArrayList<>(grouped.values()))
                .pagination(Pagination.of(page, size, total))
                .build();
    }

    @Override
    public CategoryDetailWithProductsResponse getCategoryDetailWithProducts(
            Long categoryId, int page, int size) {

        CategoryMetaInfoResponse categoryMeta =
                categoryQueryMapper.selectCategoryBasic(categoryId);
        if (categoryMeta == null) {
            throw new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }

        Long topCategoryId = categoryQueryMapper.selectTopCategoryIdByCategoryId(categoryId);
        TopCategoryOnlyResponse topCategory =
                categoryQueryMapper.selectTopCategoryBasic(topCategoryId);

        int offset = (page - 1) * size;
        long total = categoryQueryMapper.countProductsByCategoryId(categoryId);

        List<ProductDto> products =
                categoryQueryMapper.selectProductsByCategoryId(categoryId, offset, size);

        CategoryMetaInfoWithCountResponse categoryMetaWithCount = CategoryMetaInfoWithCountResponse.builder()
                .categoryId(categoryMeta.getCategoryId())
                .categoryName(categoryMeta.getCategoryName())
                .createdAt(categoryMeta.getCreatedAt())
                .modifiedAt(categoryMeta.getModifiedAt())
                .productCount(total)
                .build();

        TopCategoryOnlyWithCountResponse topCategoryWithCount = TopCategoryOnlyWithCountResponse.builder()
                .topCategoryId(topCategory.getTopCategoryId())
                .topCategoryName(topCategory.getTopCategoryName())
                .createdAt(topCategory.getCreatedAt())
                .modifiedAt(topCategory.getModifiedAt())
                .productCount(categoryQueryMapper.countProductsByTopCategoryId(topCategoryId))
                .build();

        return CategoryDetailWithProductsResponse.builder()
                .categoryMeta(categoryMetaWithCount)
                .topCategory(topCategoryWithCount)
                .products(products)
                .pagination(Pagination.of(page, size, total))
                .build();
    }

    @Override
    public List<TopCategoryDto> getAllTopCategories() {
        return topCategoryRepository.findAll().stream()
                .map(TopCategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

}

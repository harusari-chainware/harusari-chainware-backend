package com.harusari.chainware.category.query.service;

import com.harusari.chainware.category.query.dto.request.CategorySearchRequest;
import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.category.query.mapper.CategoryQueryMapper;
import com.harusari.chainware.exception.category.CategoryNotFoundException;
import com.harusari.chainware.exception.category.TopCategoryNotFoundException;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("[카테고리 - query service] CategoryQueryServiceImpl 테스트")
@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceImplTest {

    @Mock
    private CategoryQueryMapper categoryQueryMapper;

    @InjectMocks
    private CategoryQueryServiceImpl categoryQueryService;

    @Test
    @DisplayName("[전체 카테고리 조회] 전체 카테고리 목록 조회 성공")
    void searchCategories_success() {
        // given
        CategorySearchRequest request = CategorySearchRequest.builder()
                .page(1)
                .size(10)
                .build();

        CategoryWithTopResponse mockRow = CategoryWithTopResponse.builder()
                .topCategoryId(1L)
                .topCategoryName("식품")
                .categoryId(101L)
                .categoryName("과자")
                .productCount(3L)
                .build();

        given(categoryQueryMapper.searchCategoriesWithTopAndProductCount(any(), anyInt(), anyInt()))
                .willReturn(List.of(mockRow));
        given(categoryQueryMapper.countCategoriesWithCondition(any())).willReturn(1L);

        // when
        TopCategoryListResponse result = categoryQueryService.searchCategories(request);

        // then
        assertThat(result.getTopCategories()).hasSize(1);
        assertThat(result.getTopCategories().get(0).getTopCategoryName()).isEqualTo("식품");
    }

    @Test
    @DisplayName("[상위 카테고리 조회] 특정 상위 카테고리 제품 목록 조회 성공")
    void getTopCategoryWithPagedProducts_success() {
        // given
        Long topCategoryId = 1L;

        given(categoryQueryMapper.selectTopCategoryBasic(topCategoryId))
                .willReturn(TopCategoryOnlyResponse.builder()
                        .topCategoryId(topCategoryId)
                        .topCategoryName("식품")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build());

        given(categoryQueryMapper.countProductsByTopCategoryId(topCategoryId)).willReturn(2L);

        ProductDto product = ProductDto.builder()
                .productId(1L)
                .productName("과자1")
                .categoryId(10L)
                .build();

        given(categoryQueryMapper.selectProductsByTopCategoryId(eq(topCategoryId), anyInt(), anyInt()))
                .willReturn(List.of(product));

        given(categoryQueryMapper.selectCategoryProductCountsByTopCategoryId(topCategoryId))
                .willReturn(List.of(CategoryProductCountResponse.builder()
                        .categoryId(10L)
                        .productCount(2L)
                        .build()));

        given(categoryQueryMapper.selectAllCategoriesByTopCategoryId(topCategoryId))
                .willReturn(List.of(CategoryMetaInfoResponse.builder()
                        .categoryId(10L)
                        .categoryName("스낵")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build()));

        // when
        TopCategoryProductPageResponse result =
                categoryQueryService.getTopCategoryWithPagedProducts(topCategoryId, 1, 10);

        // then
        assertThat(result.getTopCategoryId()).isEqualTo(topCategoryId);
        assertThat(result.getCategories()).hasSize(1);
        assertThat(result.getCategories().get(0).getProducts()).hasSize(1);
    }

    @Test
    @DisplayName("[상위 카테고리 조회] 특정 상위 카테고리 제품 목록 조회 실패 - 존재하지 않는 ID")
    void getTopCategoryWithPagedProducts_notFound() {
        // given
        given(categoryQueryMapper.selectTopCategoryBasic(anyLong())).willReturn(null);

        // when / then
        assertThatThrownBy(() -> categoryQueryService.getTopCategoryWithPagedProducts(999L, 1, 10))
                .isInstanceOf(TopCategoryNotFoundException.class);
    }

    @Test
    @DisplayName("[카테고리 조회] 특정 카테고리 상세 조회 성공")
    void getCategoryDetailWithProducts_success() {
        // given
        Long categoryId = 10L;
        Long topCategoryId = 1L;

        given(categoryQueryMapper.selectCategoryBasic(categoryId)).willReturn(
                CategoryMetaInfoResponse.builder()
                        .categoryId(categoryId)
                        .categoryName("스낵")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build());

        given(categoryQueryMapper.selectTopCategoryIdByCategoryId(categoryId)).willReturn(topCategoryId);

        given(categoryQueryMapper.selectTopCategoryBasic(topCategoryId)).willReturn(
                TopCategoryOnlyResponse.builder()
                        .topCategoryId(topCategoryId)
                        .topCategoryName("식품")
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build());

        given(categoryQueryMapper.countProductsByCategoryId(categoryId)).willReturn(1L);

        ProductDto product = ProductDto.builder()
                .productId(1L)
                .productName("초코칩")
                .categoryId(categoryId)
                .build();

        given(categoryQueryMapper.selectProductsByCategoryId(eq(categoryId), anyInt(), anyInt()))
                .willReturn(List.of(product));

        given(categoryQueryMapper.countProductsByTopCategoryId(topCategoryId)).willReturn(10L);

        // when
        CategoryDetailWithProductsResponse result = categoryQueryService.getCategoryDetailWithProducts(categoryId, 1, 10);

        // then
        assertThat(result.getCategoryMeta().getCategoryId()).isEqualTo(categoryId);
        assertThat(result.getProducts()).hasSize(1);
    }

    @Test
    @DisplayName("[카테고리 조회] 특정 카테고리 상세 조회 실패 - 존재하지 않는 카테고리")
    void getCategoryDetailWithProducts_notFound() {
        // given
        given(categoryQueryMapper.selectCategoryBasic(anyLong())).willReturn(null);

        // when / then
        assertThatThrownBy(() -> categoryQueryService.getCategoryDetailWithProducts(999L, 1, 10))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}

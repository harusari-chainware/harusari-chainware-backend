package com.harusari.chainware.category.query.mapper;

import com.harusari.chainware.category.query.dto.request.CategorySearchRequest;
import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryQueryMapper {
    List<CategoryWithTopResponse> searchCategoriesWithTopAndProductCount(
            @Param("request") CategorySearchRequest request,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countCategoriesWithCondition(@Param("request") CategorySearchRequest request);

    TopCategoryOnlyResponse selectTopCategoryBasic(@Param("topCategoryId") Long topCategoryId);

    CategoryMetaInfoResponse selectCategoryBasic(@Param("categoryId") Long categoryId);

    Long selectTopCategoryIdByCategoryId(@Param("categoryId") Long categoryId);

    List<ProductDto> selectProductsByCategoryId(
            @Param("categoryId") Long categoryId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countProductsByCategoryId(@Param("categoryId") Long categoryId);

    List<ProductDto> selectProductsByTopCategoryId(
            @Param("topCategoryId") Long topCategoryId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countProductsByTopCategoryId(@Param("topCategoryId") Long topCategoryId);

    List<CategoryProductCountResponse> selectCategoryProductCountsByTopCategoryId(
            @Param("topCategoryId") Long topCategoryId
    );

    List<CategoryMetaInfoResponse> selectAllCategoriesByTopCategoryId(Long topCategoryId);
}
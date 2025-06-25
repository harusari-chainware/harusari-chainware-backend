package com.harusari.chainware.category.query.mapper;

import com.harusari.chainware.category.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryQueryMapper {

    List<TopCategoryOnlyResponse> selectTopCategories();

    List<CategoryResponse> selectCategoriesByTopCategoryId(@Param("topCategoryId") Long topCategoryId);

    TopCategoryOnlyResponse selectTopCategoryBasic(@Param("topCategoryId") Long topCategoryId);

    List<CategoryWithProductsResponse> selectCategoriesWithProductsByTopCategoryId(@Param("topCategoryId") Long topCategoryId);

    CategoryDetailResponse selectCategoryDetailWithProducts(@Param("categoryId") Long categoryId);

}
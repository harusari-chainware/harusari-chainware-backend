package com.harusari.chainware.category.query.mapper;

import com.harusari.chainware.category.query.dto.response.*;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryQueryMapper {
    List<CategoryWithTopResponse> selectCategoriesWithTopAndProductCount(@Param("offset") int offset, @Param("limit") int limit);

    long countAllCategories();

    TopCategoryOnlyResponse selectTopCategoryBasic(@Param("topCategoryId") Long topCategoryId);

    CategoryDetailInfoResponse selectCategoryInfoWithTop(@Param("categoryId") Long categoryId);

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

    @Select("SELECT category_name FROM category WHERE category_id = #{categoryId}")
    String selectCategoryNameById(@Param("categoryId") Long categoryId);
}
package com.harusari.chainware.category.command.application.service;

import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;
import com.harusari.chainware.category.command.domain.aggregate.Category;
import com.harusari.chainware.category.command.domain.repository.CategoryRepository;
import com.harusari.chainware.category.command.domain.repository.TopCategoryRepository;
import com.harusari.chainware.exception.category.*;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final TopCategoryRepository topCategoryRepository;
    private final ProductRepository productRepository;

    /** 상위 카테고리 존재 여부 검증 */
    private void validateTopCategoryExists(Long topCategoryId) {
        if (!topCategoryRepository.existsById(topCategoryId)) {
            throw new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
        }
    }

    /** 중복 카테고리명 확인 (같은 상위 카테고리 내) */
    private void validateDuplicateCategoryName(Long topCategoryId, String categoryName) {
        if (categoryRepository.existsByTopCategoryIdAndCategoryName(topCategoryId, categoryName)) {
            throw new CategoryNameAlreadyExistsException(CategoryErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
        }
    }

    /** 카테고리 생성 */
    @Transactional
    @Override
    public CategoryCommandResponse createCategory(CategoryCreateRequest request) {
        validateTopCategoryExists(request.getTopCategoryId());
        validateDuplicateCategoryName(request.getTopCategoryId(), request.getCategoryName());

        Category category = Category.builder()
                .topCategoryId(request.getTopCategoryId())
                .categoryName(request.getCategoryName())
                .categoryCode(request.getCategoryCode())
                .build();

        Category saved = categoryRepository.save(category);

        return CategoryCommandResponse.builder()
                .categoryId(saved.getCategoryId())
                .topCategoryId(saved.getTopCategoryId())
                .categoryName(saved.getCategoryName())
                .categoryCode(saved.getCategoryCode())
                .build();
    }

    /** 카테고리 수정 */
    @Transactional
    @Override
    public CategoryCommandResponse updateCategory(Long categoryId, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        validateTopCategoryExists(request.getTopCategoryId());

        // 이름 또는 상위 카테고리 변경 시 중복 검사
        if (!category.getCategoryName().equals(request.getCategoryName())
                || !category.getTopCategoryId().equals(request.getTopCategoryId())) {
            validateDuplicateCategoryName(request.getTopCategoryId(), request.getCategoryName());
        }

        category.updateCategoryName(request.getCategoryName());
        category.updateTopCategoryId(request.getTopCategoryId());
        category.updateCategoryCode(request.getCategoryCode());

        Category updated = categoryRepository.save(category);

        return CategoryCommandResponse.builder()
                .categoryId(updated.getCategoryId())
                .topCategoryId(updated.getTopCategoryId())
                .categoryName(updated.getCategoryName())
                .categoryCode(updated.getCategoryCode())
                .build();
    }

    /** 카테고리 삭제 */
    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new CategoryCannotDeleteHasProductsException(CategoryErrorCode.CATEGORY_CANNOT_DELETE_HAS_PRODUCTS);
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }
}
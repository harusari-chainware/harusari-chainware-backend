package com.harusari.chainware.category.command.application.service;

import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;

public interface CategoryCommandService {

    CategoryCommandResponse createCategory(CategoryCreateRequest request);

    CategoryCommandResponse updateCategory(Long categoryId, CategoryCreateRequest request);

    void deleteCategory(Long categoryId);

}

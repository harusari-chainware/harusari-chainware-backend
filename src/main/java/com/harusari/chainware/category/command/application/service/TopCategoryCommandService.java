package com.harusari.chainware.category.command.application.service;

import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;

public interface TopCategoryCommandService {

    TopCategoryCommandResponse createTopCategory(TopCategoryCreateRequest request);

    TopCategoryCommandResponse updateTopCategory(Long topCategoryId, TopCategoryUpdateRequest request);

    void deleteTopCategory(Long topCategoryId);

}

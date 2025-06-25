package com.harusari.chainware.category.command.application.service;

import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
import com.harusari.chainware.category.command.domain.aggregate.TopCategory;
import com.harusari.chainware.category.command.domain.repository.TopCategoryRepository;
import com.harusari.chainware.category.command.infrastructure.JpaCategoryRepository;
import com.harusari.chainware.category.command.infrastructure.JpaTopCategoryRepository;
import com.harusari.chainware.exception.category.CategoryErrorCode;
import com.harusari.chainware.exception.category.TopCategoryCannotDeleteHasProductsException;
import com.harusari.chainware.exception.category.TopCategoryNameAlreadyExistsException;
import com.harusari.chainware.exception.category.TopCategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopCategoryCommandServiceImpl implements TopCategoryCommandService {

    private final TopCategoryRepository topCategoryRepository;
    private final JpaTopCategoryRepository jpaTopCategoryRepository;
    private final JpaCategoryRepository jpaCategoryRepository;

    /** 상위 카테고리 생성 */
    @Transactional
    @Override
    public TopCategoryCommandResponse createTopCategory(TopCategoryCreateRequest request) {
        // 중복 이름 체크
        if (jpaTopCategoryRepository.existsByTopCategoryName(request.topCategoryName())) {
            throw new TopCategoryNameAlreadyExistsException(CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS);
        }

        TopCategory topCategory = new TopCategory(request.topCategoryName());
        TopCategory saved = topCategoryRepository.save(topCategory);

        return TopCategoryCommandResponse.builder()
                .topCategoryId(saved.getTopCategoryId())
                .topCategoryName(saved.getTopCategoryName())
                .build();
    }

    /** 상위 카테고리 이름 수정 */
    @Transactional
    @Override
    public TopCategoryCommandResponse updateTopCategory(Long topCategoryId, TopCategoryUpdateRequest request) {
        TopCategory topCategory = topCategoryRepository.findByTopCategoryId(topCategoryId)
                .orElseThrow(() -> new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND));

        // 수정하려는 이름이 이미 다른 카테고리에서 사용 중인지 확인 (자기 자신 제외)
        if (jpaTopCategoryRepository.existsByTopCategoryNameAndTopCategoryIdNot(request.topCategoryName(), topCategoryId)) {
            throw new TopCategoryNameAlreadyExistsException(CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS);
        }

        topCategory.updateTopCategoryName(request.topCategoryName());

        return TopCategoryCommandResponse.builder()
                .topCategoryId(topCategory.getTopCategoryId())
                .topCategoryName(topCategory.getTopCategoryName())
                .build();
    }

    /** 상위 카테고리 이름 삭제 */
    @Transactional
    @Override
    public void deleteTopCategory(Long topCategoryId) {
        TopCategory topCategory = topCategoryRepository.findByTopCategoryId(topCategoryId)
                .orElseThrow(() -> new TopCategoryNotFoundException(CategoryErrorCode.TOP_CATEGORY_NOT_FOUND));

        if (jpaCategoryRepository.existsByTopCategoryId(topCategoryId)) {
            throw new TopCategoryCannotDeleteHasProductsException(CategoryErrorCode.TOP_CATEGORY_CANNOT_DELETE_HAS_PRODUCTS);
        }

        topCategoryRepository.delete(topCategory);
    }
}
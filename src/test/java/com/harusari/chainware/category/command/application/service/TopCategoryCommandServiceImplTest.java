package com.harusari.chainware.category.command.application.service;

import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
import com.harusari.chainware.category.command.domain.aggregate.TopCategory;
import com.harusari.chainware.category.command.infrastructure.JpaCategoryRepository;
import com.harusari.chainware.category.command.infrastructure.JpaTopCategoryRepository;
import com.harusari.chainware.exception.category.CategoryErrorCode;
import com.harusari.chainware.exception.category.TopCategoryCannotDeleteHasProductsException;
import com.harusari.chainware.exception.category.TopCategoryNameAlreadyExistsException;
import com.harusari.chainware.exception.category.TopCategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[상위 카테고리 - service] TopCategoryCommandServiceImpl 테스트")
class TopCategoryCommandServiceImplTest {

    @InjectMocks
    private TopCategoryCommandServiceImpl service;

    @Mock
    private JpaTopCategoryRepository jpaTopCategoryRepository;

    @Mock
    private JpaCategoryRepository jpaCategoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[상위 카테고리 생성] 성공 테스트")
    void createTopCategorySuccess() {
        // given
        String name = "전자제품";
        TopCategoryCreateRequest request = TopCategoryCreateRequest.builder()
                .topCategoryName(name)
                .build();

        TopCategory saved = TopCategory.builder().topCategoryName(name).build();
        ReflectionTestUtils.setField(saved, "topCategoryId", 42L);

        given(jpaTopCategoryRepository.existsByTopCategoryName(name)).willReturn(false);
        given(jpaTopCategoryRepository.save(any(TopCategory.class))).willReturn(saved);

        // when
        TopCategoryCommandResponse response = service.createTopCategory(request);

        // then
        assertThat(response.getTopCategoryId()).isEqualTo(42L);
        assertThat(response.getTopCategoryName()).isEqualTo(name);
    }

    @Test
    @DisplayName("[상위 카테고리 생성] 이름 중복 예외")
    void createTopCategoryDuplicateName() {
        // given
        String name = "전자제품";
        TopCategoryCreateRequest request = TopCategoryCreateRequest.builder()
                .topCategoryName(name)
                .build();

        given(jpaTopCategoryRepository.existsByTopCategoryName(name)).willReturn(true);

        // when / then
        assertThatThrownBy(() -> service.createTopCategory(request))
                .isInstanceOf(TopCategoryNameAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("[상위 카테고리 수정] 성공 테스트")
    void updateTopCategorySuccess() {
        // given
        Long id = 7L;
        TopCategory existing = TopCategory.builder()
                .topCategoryName("구가전")
                .build();
        ReflectionTestUtils.setField(existing, "topCategoryId", id);

        String newName = "가전제품";
        TopCategoryUpdateRequest request = new TopCategoryUpdateRequest(newName);

        given(jpaTopCategoryRepository.findByTopCategoryId(id)).willReturn(Optional.of(existing));
        given(jpaTopCategoryRepository.existsByTopCategoryNameAndTopCategoryIdNot(newName, id)).willReturn(false);

        // when
        TopCategoryCommandResponse response = service.updateTopCategory(id, request);

        // then
        assertThat(response.getTopCategoryId()).isEqualTo(id);
        assertThat(response.getTopCategoryName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("[상위 카테고리 수정] 없는 ID 예외")
    void updateTopCategoryNotFound() {
        // given
        Long id = 99L;
        TopCategoryUpdateRequest request = new TopCategoryUpdateRequest("아무개");

        given(jpaTopCategoryRepository.findByTopCategoryId(id)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.updateTopCategory(id, request))
                .isInstanceOf(TopCategoryNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[상위 카테고리 수정] 이름 중복 예외")
    void updateTopCategoryDuplicateName() {
        // given
        Long id = 7L;
        TopCategory existing = TopCategory.builder()
                .topCategoryName("구가전")
                .build();
        ReflectionTestUtils.setField(existing, "topCategoryId", id);

        String newName = "가전제품";
        TopCategoryUpdateRequest request = new TopCategoryUpdateRequest(newName);

        given(jpaTopCategoryRepository.findByTopCategoryId(id)).willReturn(Optional.of(existing));
        given(jpaTopCategoryRepository.existsByTopCategoryNameAndTopCategoryIdNot(newName, id)).willReturn(true);

        // when / then
        assertThatThrownBy(() -> service.updateTopCategory(id, request))
                .isInstanceOf(TopCategoryNameAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("[상위 카테고리 삭제] 성공 테스트")
    void deleteTopCategorySuccess() {
        // given
        Long id = 5L;
        TopCategory existing = TopCategory.builder()
                .topCategoryName("식품")
                .build();
        ReflectionTestUtils.setField(existing, "topCategoryId", id);

        given(jpaTopCategoryRepository.findByTopCategoryId(id)).willReturn(Optional.of(existing));
        given(jpaCategoryRepository.existsByTopCategoryId(id)).willReturn(false);

        // when
        service.deleteTopCategory(id);

        // then
        then(jpaTopCategoryRepository).should().delete(existing);
    }

    @Test
    @DisplayName("[상위 카테고리 삭제] 없는 ID 예외")
    void deleteTopCategoryNotFound() {
        // given
        Long id = 123L;
        given(jpaTopCategoryRepository.findByTopCategoryId(id)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.deleteTopCategory(id))
                .isInstanceOf(TopCategoryNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[상위 카테고리 삭제] 하위 카테고리 존재 예외")
    void deleteTopCategoryHasProducts() {
        // given
        Long id = 5L;
        TopCategory existing = TopCategory.builder()
                .topCategoryName("식품")
                .build();
        ReflectionTestUtils.setField(existing, "topCategoryId", id);

        given(jpaTopCategoryRepository.findByTopCategoryId(id)).willReturn(Optional.of(existing));
        given(jpaCategoryRepository.existsByTopCategoryId(id)).willReturn(true);

        // when / then
        assertThatThrownBy(() -> service.deleteTopCategory(id))
                .isInstanceOf(TopCategoryCannotDeleteHasProductsException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_CANNOT_DELETE_HAS_PRODUCTS);
    }
}

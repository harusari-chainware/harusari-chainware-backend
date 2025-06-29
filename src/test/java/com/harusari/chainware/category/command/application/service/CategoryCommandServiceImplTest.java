package com.harusari.chainware.category.command.application.service;

import com.harusari.chainware.category.command.application.dto.request.CategoryCreateRequest;
import com.harusari.chainware.category.command.application.dto.response.CategoryCommandResponse;
import com.harusari.chainware.category.command.domain.aggregate.Category;
import com.harusari.chainware.category.command.infrastructure.JpaCategoryRepository;
import com.harusari.chainware.category.command.infrastructure.JpaTopCategoryRepository;
import com.harusari.chainware.exception.category.*;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("[카테고리 - service] CategoryCommandServiceImpl 테스트")
class CategoryCommandServiceImplTest {

    @InjectMocks
    private CategoryCommandServiceImpl categoryService;

    @Mock
    private JpaCategoryRepository jpaCategoryRepository;

    @Mock
    private JpaTopCategoryRepository jpaTopCategoryRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- createCategory ---

    @Test
    @DisplayName("[카테고리 생성] 성공 테스트")
    void createCategorySuccess() {
        // given
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .topCategoryId(1L)
                .categoryName("NewCat")
                .categoryCode("NC01")
                .build();

        given(jpaTopCategoryRepository.existsById(1L)).willReturn(true);
        given(jpaCategoryRepository.existsByTopCategoryIdAndCategoryName(1L, "NewCat"))
                .willReturn(false);

        Category saved = Category.builder()
                .topCategoryId(1L)
                .categoryName("NewCat")
                .categoryCode("NC01")
                .build();
        ReflectionTestUtils.setField(saved, "categoryId", 100L);
        given(jpaCategoryRepository.save(any(Category.class))).willReturn(saved);

        // when
        CategoryCommandResponse response = categoryService.createCategory(request);

        // then
        assertThat(response.getCategoryId()).isEqualTo(100L);
        assertThat(response.getTopCategoryId()).isEqualTo(1L);
        assertThat(response.getCategoryName()).isEqualTo("NewCat");
        assertThat(response.getCategoryCode()).isEqualTo("NC01");
    }

    @Test
    @DisplayName("[카테고리 생성] 상위 카테고리 없음 예외 테스트")
    void createCategoryTopNotFound() {
        // given
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .topCategoryId(9L)
                .categoryName("X")
                .build();
        given(jpaTopCategoryRepository.existsById(9L)).willReturn(false);

        // when / then
        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(TopCategoryNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[카테고리 생성] 중복 이름 예외 테스트")
    void createCategoryDuplicateName() {
        // given
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .topCategoryId(1L)
                .categoryName("DupCat")
                .build();

        given(jpaTopCategoryRepository.existsById(1L)).willReturn(true);
        given(jpaCategoryRepository.existsByTopCategoryIdAndCategoryName(1L, "DupCat"))
                .willReturn(true);

        // when / then
        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(CategoryNameAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
    }

    // --- updateCategory ---

    @Test
    @DisplayName("[카테고리 수정] 성공 테스트")
    void updateCategorySuccess() {
        // given
        Long categoryId = 200L;
        Category existing = Category.builder()
                .topCategoryId(1L)
                .categoryName("OldName")
                .categoryCode("OC01")
                .build();
        ReflectionTestUtils.setField(existing, "categoryId", categoryId);

        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .topCategoryId(2L)
                .categoryName("NewName")
                .categoryCode("NC02")
                .build();

        given(jpaCategoryRepository.findById(categoryId)).willReturn(Optional.of(existing));
        given(jpaTopCategoryRepository.existsById(2L)).willReturn(true);
        // name or topCategory changed, so check duplicate
        given(jpaCategoryRepository.existsByTopCategoryIdAndCategoryName(2L, "NewName"))
                .willReturn(false);

        Category updated = Category.builder()
                .topCategoryId(2L)
                .categoryName("NewName")
                .categoryCode("NC02")
                .build();
        ReflectionTestUtils.setField(updated, "categoryId", categoryId);
        given(jpaCategoryRepository.save(existing)).willReturn(updated);

        // when
        CategoryCommandResponse response = categoryService.updateCategory(categoryId, request);

        // then
        assertThat(response.getCategoryId()).isEqualTo(categoryId);
        assertThat(response.getTopCategoryId()).isEqualTo(2L);
        assertThat(response.getCategoryName()).isEqualTo("NewName");
        assertThat(response.getCategoryCode()).isEqualTo("NC02");
    }

    @Test
    @DisplayName("[카테고리 수정] 카테고리 없음 예외 테스트")
    void updateCategoryNotFound() {
        // given
        given(jpaCategoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() ->
                categoryService.updateCategory(1L, mock(CategoryCreateRequest.class))
        ).isInstanceOf(CategoryNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[카테고리 수정] 상위 카테고리 없음 예외 테스트")
    void updateCategoryTopNotFound() {
        // given
        Long categoryId = 300L;
        Category existing = Category.builder()
                .topCategoryId(1L)
                .categoryName("A")
                .build();
        ReflectionTestUtils.setField(existing, "categoryId", categoryId);
        given(jpaCategoryRepository.findById(categoryId)).willReturn(Optional.of(existing));

        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .topCategoryId(9L)
                .categoryName("B")
                .build();
        given(jpaTopCategoryRepository.existsById(9L)).willReturn(false);

        // when / then
        assertThatThrownBy(() ->
                categoryService.updateCategory(categoryId, request)
        ).isInstanceOf(TopCategoryNotFoundException.class)
            .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[카테고리 수정] 중복 이름 예외 테스트")
    void updateCategoryDuplicateName() {
        // given
        Long categoryId = 400L;
        Category existing = Category.builder()
                .topCategoryId(1L)
                .categoryName("Orig")
                .build();
        ReflectionTestUtils.setField(existing, "categoryId", categoryId);
        given(jpaCategoryRepository.findById(categoryId)).willReturn(Optional.of(existing));
        given(jpaTopCategoryRepository.existsById(2L)).willReturn(true);

        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .topCategoryId(2L)
                .categoryName("DupName")
                .build();
        // name/topCategory changed -> duplicate exists
        given(jpaCategoryRepository.existsByTopCategoryIdAndCategoryName(2L, "DupName"))
                .willReturn(true);

        // when / then
        assertThatThrownBy(() ->
                categoryService.updateCategory(categoryId, request)
        ).isInstanceOf(CategoryNameAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
    }

    // --- deleteCategory ---

    @Test
    @DisplayName("[카테고리 삭제] 성공 테스트")
    void deleteCategorySuccess() {
        // given
        Long categoryId = 500L;
        // no products
        given(productRepository.existsByCategoryId(categoryId)).willReturn(false);
        Category existing = Category.builder().build();
        ReflectionTestUtils.setField(existing, "categoryId", categoryId);
        given(jpaCategoryRepository.findById(categoryId)).willReturn(Optional.of(existing));

        // when / then
        // no exception thrown
        categoryService.deleteCategory(categoryId);
        then(jpaCategoryRepository).should().delete(existing);
    }

    @Test
    @DisplayName("[카테고리 삭제] 제품 존재 예외 테스트")
    void deleteCategoryHasProducts() {
        // given
        Long categoryId = 600L;
        given(productRepository.existsByCategoryId(categoryId)).willReturn(true);

        // when / then
        assertThatThrownBy(() -> categoryService.deleteCategory(categoryId))
                .isInstanceOf(CategoryCannotDeleteHasProductsException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.CATEGORY_CANNOT_DELETE_HAS_PRODUCTS);
    }

    @Test
    @DisplayName("[카테고리 삭제] 카테고리 없음 예외 테스트")
    void deleteCategoryNotFound() {
        // given
        Long categoryId = 700L;
        given(productRepository.existsByCategoryId(categoryId)).willReturn(false);
        given(jpaCategoryRepository.findById(categoryId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> categoryService.deleteCategory(categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.CATEGORY_NOT_FOUND);
    }
}
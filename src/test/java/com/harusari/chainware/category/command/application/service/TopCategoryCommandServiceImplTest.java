//package com.harusari.chainware.category.command.application.service;
//
//import com.harusari.chainware.category.command.application.dto.request.TopCategoryCreateRequest;
//import com.harusari.chainware.category.command.application.dto.request.TopCategoryUpdateRequest;
//import com.harusari.chainware.category.command.application.dto.response.TopCategoryCommandResponse;
//import com.harusari.chainware.category.command.domain.aggregate.TopCategory;
//import com.harusari.chainware.category.command.domain.repository.TopCategoryRepository;
//import com.harusari.chainware.category.command.infrastructure.JpaCategoryRepository;
//import com.harusari.chainware.category.command.infrastructure.JpaTopCategoryRepository;
//import com.harusari.chainware.exception.category.CategoryErrorCode;
//import com.harusari.chainware.exception.category.TopCategoryCannotDeleteHasProductsException;
//import com.harusari.chainware.exception.category.TopCategoryNameAlreadyExistsException;
//import com.harusari.chainware.exception.category.TopCategoryNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.*;
//
//@DisplayName("[상위 카테고리 - service] TopCategoryCommandServiceImpl 테스트")
//class TopCategoryCommandServiceImplTest {
//
//    @InjectMocks
//    private TopCategoryCommandServiceImpl topCategoryService;
//
//    @Mock
//    private TopCategoryRepository topCategoryRepository;
//
//    @Mock
//    private JpaTopCategoryRepository jpaTopCategoryRepository;
//
//    @Mock
//    private JpaCategoryRepository jpaCategoryRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // --- createTopCategory ---
//
//    @Test
//    @DisplayName("[상위 카테고리 생성] 성공 테스트")
//    void createTopCategorySuccess() {
//        // given
//        TopCategoryCreateRequest request = TopCategoryCreateRequest.builder()
//                .topCategoryName("식품")
//                .build();
//
//        given(jpaTopCategoryRepository.existsByTopCategoryName("식품")).willReturn(false);
//
//        TopCategory saved = TopCategory.builder().topCategoryName("식품").build();
//        ReflectionTestUtils.setField(saved, "topCategoryId", 1L);
//
//        given(topCategoryRepository.save(any(TopCategory.class))).willReturn(saved);
//
//        // when
//        TopCategoryCommandResponse response = topCategoryService.createTopCategory(request);
//
//        // then
//        assertThat(response.getTopCategoryId()).isEqualTo(1L);
//        assertThat(response.getTopCategoryName()).isEqualTo("식품");
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 생성] 중복 이름 예외 테스트")
//    void createTopCategoryDuplicateName() {
//        // given
//        TopCategoryCreateRequest request = TopCategoryCreateRequest.builder()
//                .topCategoryName("중복")
//                .build();
//        given(jpaTopCategoryRepository.existsByTopCategoryName("중복")).willReturn(true);
//
//        // when / then
//        assertThatThrownBy(() -> topCategoryService.createTopCategory(request))
//                .isInstanceOf(TopCategoryNameAlreadyExistsException.class)
//                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS);
//    }
//
//    // --- updateTopCategory ---
//
//    @Test
//    @DisplayName("[상위 카테고리 수정] 성공 테스트")
//    void updateTopCategorySuccess() {
//        // given
//        Long topCategoryId = 10L;
//        TopCategory existing = TopCategory.builder().topCategoryName("기존이름").build();
//        ReflectionTestUtils.setField(existing, "topCategoryId", topCategoryId);
//
//        given(topCategoryRepository.findByTopCategoryId(topCategoryId)).willReturn(Optional.of(existing));
//        given(jpaTopCategoryRepository.existsByTopCategoryNameAndTopCategoryIdNot("새이름", topCategoryId)).willReturn(false);
//
//        TopCategoryUpdateRequest request = new TopCategoryUpdateRequest("새이름");
//
//        // when
//        TopCategoryCommandResponse response = topCategoryService.updateTopCategory(topCategoryId, request);
//
//        // then
//        assertThat(response.getTopCategoryId()).isEqualTo(topCategoryId);
//        assertThat(response.getTopCategoryName()).isEqualTo("새이름");
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 수정] 카테고리 없음 예외 테스트")
//    void updateTopCategoryNotFound() {
//        // given
//        given(topCategoryRepository.findByTopCategoryId(anyLong())).willReturn(Optional.empty());
//
//        // when / then
//        assertThatThrownBy(() -> topCategoryService.updateTopCategory(99L, new TopCategoryUpdateRequest("아무거나")))
//                .isInstanceOf(TopCategoryNotFoundException.class)
//                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 수정] 중복 이름 예외 테스트")
//    void updateTopCategoryDuplicateName() {
//        // given
//        Long topCategoryId = 20L;
//        TopCategory existing = TopCategory.builder().topCategoryName("기존이름").build();
//        ReflectionTestUtils.setField(existing, "topCategoryId", topCategoryId);
//
//        given(topCategoryRepository.findByTopCategoryId(topCategoryId)).willReturn(Optional.of(existing));
//        given(jpaTopCategoryRepository.existsByTopCategoryNameAndTopCategoryIdNot("중복", topCategoryId)).willReturn(true);
//
//        TopCategoryUpdateRequest request = new TopCategoryUpdateRequest("중복");
//
//        // when / then
//        assertThatThrownBy(() -> topCategoryService.updateTopCategory(topCategoryId, request))
//                .isInstanceOf(TopCategoryNameAlreadyExistsException.class)
//                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NAME_ALREADY_EXISTS);
//    }
//
//    // --- deleteTopCategory ---
//
//    @Test
//    @DisplayName("[상위 카테고리 삭제] 성공 테스트")
//    void deleteTopCategorySuccess() {
//        // given
//        Long topCategoryId = 30L;
//        TopCategory existing = TopCategory.builder().topCategoryName("삭제").build();
//        ReflectionTestUtils.setField(existing, "topCategoryId", topCategoryId);
//
//        given(topCategoryRepository.findByTopCategoryId(topCategoryId)).willReturn(Optional.of(existing));
//        given(jpaCategoryRepository.existsByTopCategoryId(topCategoryId)).willReturn(false);
//
//        // when
//        topCategoryService.deleteTopCategory(topCategoryId);
//
//        // then
//        then(topCategoryRepository).should().delete(existing);
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 삭제] 카테고리 없음 예외 테스트")
//    void deleteTopCategoryNotFound() {
//        // given
//        given(topCategoryRepository.findByTopCategoryId(anyLong())).willReturn(Optional.empty());
//
//        // when / then
//        assertThatThrownBy(() -> topCategoryService.deleteTopCategory(100L))
//                .isInstanceOf(TopCategoryNotFoundException.class)
//                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_NOT_FOUND);
//    }
//
//    @Test
//    @DisplayName("[상위 카테고리 삭제] 하위 카테고리 존재 예외 테스트")
//    void deleteTopCategoryHasCategories() {
//        // given
//        Long topCategoryId = 40L;
//        TopCategory existing = TopCategory.builder().topCategoryName("테스트").build();
//        ReflectionTestUtils.setField(existing, "topCategoryId", topCategoryId);
//
//        // 서비스가 실제로 사용하는 리포지토리에 맞게 mock 세팅!
//        given(jpaTopCategoryRepository.findByTopCategoryId(eq(topCategoryId))).willReturn(Optional.of(existing));
//        given(jpaCategoryRepository.existsByTopCategoryId(eq(topCategoryId))).willReturn(true);
//
//        // when / then
//        assertThatThrownBy(() -> topCategoryService.deleteTopCategory(topCategoryId))
//                .isInstanceOf(TopCategoryCannotDeleteHasProductsException.class)
//                .hasFieldOrPropertyWithValue("errorCode", CategoryErrorCode.TOP_CATEGORY_CANNOT_DELETE_HAS_PRODUCTS);
//    }
//}

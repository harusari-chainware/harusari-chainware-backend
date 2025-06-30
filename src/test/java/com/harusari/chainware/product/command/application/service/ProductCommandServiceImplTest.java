package com.harusari.chainware.product.command.application.service;

import com.harusari.chainware.exception.product.InvalidProductStatusException;
import com.harusari.chainware.exception.product.ProductErrorCode;
import com.harusari.chainware.exception.product.ProductNotFoundException;
import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.application.dto.request.ProductUpdateRequest;
import com.harusari.chainware.product.command.application.dto.response.ProductCommandResponse;
import com.harusari.chainware.product.common.mapstruct.ProductMapStruct;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("[제품 - service] ProductCommandServiceImpl 테스트")
class ProductCommandServiceImplTest {

    @InjectMocks
    private ProductCommandServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapStruct productMapstruct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[제품 등록] 성공 테스트 (maxNumber present)")
    void createProductSuccess_withMaxNumber() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .categoryCode("CAT01")
                .productName("TestProduct")
                .basePrice(1000)
                .unitQuantity("1kg")
                .unitSpec("kg")
                .storeType(StoreType.ROOM_TEMPERATURE)
                .safetyStock(50)
                .origin("Korea")
                .shelfLife(30)
                .build();

        // 기존 이름 없음
        given(productRepository.existsByProductName("TestProduct")).willReturn(false);
        // generateProductCode: prefix PD-CAT01-, maxNumber = 5
        given(productRepository.findMaxNumberByCategoryCode("PD-CAT01-")).willReturn(5);

        // mapstruct → 엔티티
        Product mapped = Product.builder()
                .categoryId(10L)
                .productName("TestProduct")
                .basePrice(1000)
                .unitQuantity("1kg")
                .unitSpec("kg")
                .storeType(StoreType.ROOM_TEMPERATURE)
                .safetyStock(50)
                .origin("Korea")
                .shelfLife(30)
                .build();
        given(productMapstruct.toEntity(request)).willReturn(mapped);

        // 저장된 엔티티
        Product saved = Product.builder()
                .categoryId(10L)
                .productCode("PD-CAT01-6")
                .productName("TestProduct")
                .basePrice(1000)
                .unitQuantity("1kg")
                .unitSpec("kg")
                .storeType(StoreType.ROOM_TEMPERATURE)
                .safetyStock(50)
                .origin("Korea")
                .shelfLife(30)
                .productStatus(true)
                .isDeleted(false)
                .build();
        ReflectionTestUtils.setField(saved, "productId", 100L);
        given(productRepository.save(any(Product.class))).willReturn(saved);

        // when
        ProductCommandResponse response = productService.createProduct(request);

        // then
        assertThat(response.getProductId()).isEqualTo(100L);
        assertThat(response.getCategoryId()).isEqualTo(10L);
        assertThat(response.getProductCode()).isEqualTo("PD-CAT01-6");
        assertThat(response.getProductName()).isEqualTo("TestProduct");
        assertThat(response.getBasePrice()).isEqualTo(1000);
        assertThat(response.getUnitQuantity()).isEqualTo("1kg");
        assertThat(response.getUnitSpec()).isEqualTo("kg");
        assertThat(response.getStoreType()).isEqualTo(StoreType.ROOM_TEMPERATURE);
        assertThat(response.getSafetyStock()).isEqualTo(50);
        assertThat(response.getOrigin()).isEqualTo("Korea");
        assertThat(response.getShelfLife()).isEqualTo(30);
        assertThat(response.getProductStatus()).isTrue();
    }

    @Test
    @DisplayName("[제품 등록] 중복 이름 예외 테스트")
    void createProductDuplicateName() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .categoryCode("CAT01")
                .productName("DupProduct")
                .build();

        given(productRepository.existsByProductName("DupProduct")).willReturn(true);

        // when / then
        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(InvalidProductStatusException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.DUPLICATE_PRODUCT_NAME);
    }

    @Test
    @DisplayName("[제품 수정] 성공 테스트")
    void updateProductSuccess() {
        // given
        Long productId = 200L;
        Product existing = Product.builder()
                .categoryId(10L)
                .productCode("PD-CAT01-1")
                .productName("OldName")
                .basePrice(1500)
                .unitQuantity("2kg")
                .unitSpec("kg")
                .storeType(StoreType.CHILLED)
                .safetyStock(20)
                .origin("USA")
                .shelfLife(60)
                .productStatus(true)
                .isDeleted(false)
                .build();
        ReflectionTestUtils.setField(existing, "productId", productId);
        Product spyProduct = spy(existing);

        given(productRepository.findById(productId)).willReturn(Optional.of(spyProduct));
        // rename to a new name, no conflict
        given(productRepository.existsByProductName("NewName")).willReturn(false);

        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .productName("NewName")
                .basePrice(2000)
                .unitQuantity("3kg")
                .unitSpec("kg")
                .storeType(StoreType.ROOM_TEMPERATURE)
                .safetyStock(25)
                .origin("China")
                .shelfLife(90)
                .productStatus(false)
                .build();

        // when
        ProductCommandResponse response = productService.updateProduct(productId, request);

        // then
        assertThat(response.getProductId()).isEqualTo(productId);
        assertThat(response.getProductName()).isEqualTo("NewName");
        assertThat(response.getBasePrice()).isEqualTo(2000);
        assertThat(response.getUnitQuantity()).isEqualTo("3kg");
        assertThat(response.getUnitSpec()).isEqualTo("kg");
        assertThat(response.getStoreType()).isEqualTo(StoreType.ROOM_TEMPERATURE);
        assertThat(response.getSafetyStock()).isEqualTo(25);
        assertThat(response.getOrigin()).isEqualTo("China");
        assertThat(response.getShelfLife()).isEqualTo(90);
        assertThat(response.getProductStatus()).isFalse();

        then(spyProduct).should().changeStatus(false);
    }

    @Test
    @DisplayName("[제품 수정] 존재하지 않음 예외 테스트")
    void updateProductNotFound() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> productService.updateProduct(1L, mock(ProductUpdateRequest.class)))
                .isInstanceOf(ProductNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("[제품 수정] 삭제된 제품 예외 테스트")
    void updateProductAlreadyDeleted() {
        // given
        Product deleted = Product.builder()
                .isDeleted(true)
                .build();
        given(productRepository.findById(1L)).willReturn(Optional.of(deleted));

        // when / then
        assertThatThrownBy(() -> productService.updateProduct(1L, mock(ProductUpdateRequest.class)))
                .isInstanceOf(InvalidProductStatusException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.INVALID_PRODUCT_STATUS);
    }

    @Test
    @DisplayName("[제품 수정] 이름 중복 예외 테스트")
    void updateProductDuplicateName() {
        // given
        Long id = 300L;
        Product existing = Product.builder()
                .productName("OrigName")
                .isDeleted(false)
                .build();
        ReflectionTestUtils.setField(existing, "productId", id);
        given(productRepository.findById(id)).willReturn(Optional.of(existing));
        // attempted rename to same name -> skip, but test changing to other with conflict
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .productName("ConflictingName")
                .build();
        given(productRepository.existsByProductName("ConflictingName")).willReturn(true);

        // when / then
        assertThatThrownBy(() -> productService.updateProduct(id, request))
                .isInstanceOf(InvalidProductStatusException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.DUPLICATE_PRODUCT_NAME);
    }

    @Test
    @DisplayName("[제품 삭제] 성공 테스트")
    void deleteProductSuccess() {
        // given
        Long id = 400L;
        Product existing = Product.builder().build();
        ReflectionTestUtils.setField(existing, "productId", id);
        Product spyProduct = spy(existing);
        given(productRepository.findById(id)).willReturn(Optional.of(spyProduct));

        // when
        productService.deleteProduct(id);

        // then
        then(spyProduct).should().markAsDeleted();
    }

    @Test
    @DisplayName("[제품 삭제] 존재하지 않음 예외 테스트")
    void deleteProductNotFound() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProductErrorCode.PRODUCT_NOT_FOUND);
    }
}
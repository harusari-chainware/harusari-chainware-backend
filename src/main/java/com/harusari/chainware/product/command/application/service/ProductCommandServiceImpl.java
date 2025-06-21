package com.harusari.chainware.product.command.application.service;

import com.harusari.chainware.exception.product.InvalidProductStatusException;
import com.harusari.chainware.exception.product.ProductErrorCode;
import com.harusari.chainware.exception.product.ProductNotFoundException;
import com.harusari.chainware.product.command.application.dto.request.ProductCreateRequest;
import com.harusari.chainware.product.command.application.dto.request.ProductUpdateRequest;
import com.harusari.chainware.product.command.application.dto.response.ProductCommandResponse;
import com.harusari.chainware.product.command.application.mapper.ProductMapper;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /* 상품 등록 */
    @Transactional
    @Override
    public ProductCommandResponse createProduct(ProductCreateRequest request) {

        if (productRepository.existsByProductName(request.getProductName())) {
            throw new InvalidProductStatusException(ProductErrorCode.DUPLICATE_PRODUCT_NAME);
        }

        String productCode = generateProductCode(request.getCategoryCode());

        Product newProduct = productMapper.toEntity(request);

        Product productWithCode = Product.builder()
                .categoryId(newProduct.getCategoryId())
                .productName(newProduct.getProductName())
                .productCode(productCode)
                .basePrice(newProduct.getBasePrice())
                .unitQuantity(newProduct.getUnitQuantity())
                .unitSpec(newProduct.getUnitSpec())
                .storeType(newProduct.getStoreType())
                .safetyStock(newProduct.getSafetyStock())
                .origin(newProduct.getOrigin())
                .shelfLife(newProduct.getShelfLife())
                .productStatus(true)
                .isDeleted(false)
                .build();

        Product saved = productRepository.save(productWithCode);

        return ProductCommandResponse.builder()
                .productId(saved.getProductId())
                .categoryId(saved.getCategoryId())
                .productCode(saved.getProductCode())
                .productName(saved.getProductName())
                .unitQuantity(saved.getUnitQuantity())
                .unitSpec(saved.getUnitSpec())
                .basePrice(saved.getBasePrice())
                .storeType(saved.getStoreType())
                .safetyStock(saved.getSafetyStock())
                .origin(saved.getOrigin())
                .shelfLife(saved.getShelfLife())
                .productStatus(saved.getProductStatus())
                .build();
    }

    /* 상품 수정 */
    @Transactional
    @Override
    public ProductCommandResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (Boolean.TRUE.equals(product.getIsDeleted())) {
            throw new InvalidProductStatusException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }

        String requestedName = request.getProductName();
        if (requestedName != null && !requestedName.equals(product.getProductName())) {
            if (productRepository.existsByProductName(requestedName)) {
                throw new InvalidProductStatusException(ProductErrorCode.DUPLICATE_PRODUCT_NAME);
            }
        }

        // 기존 값 유지하면서 null 아닌 항목만 덮어쓰기
        String updatedName = request.getProductName() != null ? request.getProductName() : product.getProductName();
        Integer updatedPrice = request.getBasePrice() != null ? request.getBasePrice() : product.getBasePrice();
        String updatedQuantity = request.getUnitQuantity() != null ? request.getUnitQuantity() : product.getUnitQuantity();
        String updatedSpec = request.getUnitSpec() != null ? request.getUnitSpec() : product.getUnitSpec();
        StoreType updatedStoreType = request.getStoreType() != null ? request.getStoreType() : product.getStoreType();
        Integer updatedSafetyStock = request.getSafetyStock() != null ? request.getSafetyStock() : product.getSafetyStock();
        String updatedOrigin = request.getOrigin() != null ? request.getOrigin() : product.getOrigin();
        Integer updatedShelfLife = request.getShelfLife() != null ? request.getShelfLife() : product.getShelfLife();
        Boolean updatedStatus = request.getProductStatus() != null ? request.getProductStatus() : product.getProductStatus();

        product.updateProductDetails(
                updatedName,
                updatedPrice,
                updatedQuantity,
                updatedSpec,
                updatedStoreType,
                updatedSafetyStock,
                updatedOrigin,
                updatedShelfLife,
                updatedStatus
        );

        product.changeStatus(updatedStatus);

        // 응답 DTO 생성 후 반환
        return ProductCommandResponse.builder()
                .productId(product.getProductId())
                .categoryId(product.getCategoryId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .basePrice(product.getBasePrice())
                .unitQuantity(product.getUnitQuantity())
                .unitSpec(product.getUnitSpec())
                .storeType(product.getStoreType())
                .safetyStock(product.getSafetyStock())
                .origin(product.getOrigin())
                .shelfLife(product.getShelfLife())
                .productStatus(product.getProductStatus())
                .build();
    }

    /* 상품 삭제 (soft delete + 상태 비활성화) */
    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.markAsDeleted();
    }

    /**
     * productCode 자동 생성 메서드
     * 형식: pd-(categoryCode)-(숫자)
     * 숫자는 categoryCode에 해당하는 기존 최대 숫자 + 1
     */
    private String generateProductCode(String categoryCode) {
        String prefix = "pd-" + categoryCode + "-";
        Integer maxNumber = productRepository.findMaxNumberByCategoryCode(prefix);
        int nextNumber = (maxNumber == null) ? 1 : maxNumber + 1;

        return prefix + nextNumber;
    }
}
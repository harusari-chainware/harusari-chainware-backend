package com.harusari.chainware.product.command.domain.aggregate;

import com.harusari.chainware.exception.product.InvalidProductStatusException;
import com.harusari.chainware.exception.product.ProductErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")  // DB 테이블명
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE product SET is_deleted = 1 WHERE product_id = ?")
@Where(clause = "is_deleted = 0")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "unit_quantity")
    private String unitQuantity;

    @Column(name = "unit_spec")
    private String unitSpec;

    @Column(name = "base_price")
    private Integer basePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_type")
    private StoreType storeType;

    @Column(name = "safety_stock")
    private Integer safetyStock;

    @Column(name = "origin")
    private String origin;

    @Column(name = "shelf_life")
    private Integer shelfLife;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "product_status")
    private Boolean productStatus = true;

    // 상품 정보 업데이트 메서드
    public void updateProductDetails(
            String productName,
            Integer basePrice,
            String unitQuantity,
            String unitSpec,
            StoreType storeType,
            Integer safetyStock,
            String origin,
            Integer shelfLife,
            Boolean productStatus
    ) {
        this.productName = productName;
        this.basePrice = basePrice;
        this.unitQuantity = unitQuantity;
        this.unitSpec = unitSpec;
        this.storeType = storeType;
        this.safetyStock = safetyStock;
        this.origin = origin;
        this.shelfLife = shelfLife;
        this.productStatus = (productStatus != null) ? productStatus : true;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    @Builder
    public Product(
            Long productId,
            Long categoryId,
            String productName,
            String productCode,
            Integer basePrice,
            String unitQuantity,
            String unitSpec,
            StoreType storeType,
            Integer safetyStock,
            String origin,
            Integer shelfLife,
            Boolean productStatus,
            Boolean isDeleted
    ) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productCode = productCode;
        this.basePrice = basePrice;
        this.unitQuantity = unitQuantity;
        this.unitSpec = unitSpec;
        this.storeType = storeType;
        this.safetyStock = safetyStock;
        this.origin = origin;
        this.shelfLife = shelfLife;
        this.productStatus = (productStatus != null) ? productStatus : true;
        this.isDeleted = (isDeleted != null) ? isDeleted : false;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.productStatus = false;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void changeStatus(Boolean status) {
        if (Boolean.TRUE.equals(this.isDeleted)) {
            throw new InvalidProductStatusException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        this.productStatus = status;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

}
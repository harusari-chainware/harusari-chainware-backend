package com.harusari.chainware.category.command.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "top_category_id")
    private Long topCategoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_code")
    private String categoryCode;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public Category(Long topCategoryId, String categoryName, String categoryCode) {
        this.topCategoryId = topCategoryId;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
    }

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void updateTopCategoryId(Long topCategoryId) {
        this.topCategoryId = topCategoryId;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void updateCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }
}

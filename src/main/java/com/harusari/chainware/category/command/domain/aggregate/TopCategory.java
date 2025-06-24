package com.harusari.chainware.category.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "top_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class TopCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "top_category_id")
    private Long topCategoryId;

    @Column(name = "top_category_name")
    private String topCategoryName;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public TopCategory(String topCategoryName) {
        this.topCategoryName = topCategoryName;
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    public void updateTopCategoryName(String newName) {
        this.topCategoryName = newName;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

}
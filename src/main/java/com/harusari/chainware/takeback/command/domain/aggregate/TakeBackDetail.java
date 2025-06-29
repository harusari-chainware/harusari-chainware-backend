package com.harusari.chainware.takeback.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "take_back_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TakeBackDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_detail_id")
    private Long takeBackDetailId;

    @Column(name = "take_back_id", nullable = false)
    private Long takeBackId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "take_back_reason", nullable = false)
    private String takeBackReason;

    @Column(name = "take_back_image", nullable = false)
    private String takeBackImage;

    @Builder
    public TakeBackDetail(Long takeBackId, Long productId, Integer quantity,
                          Integer price, String takeBackReason, String takeBackImage) {
        this.takeBackId = takeBackId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.takeBackReason = takeBackReason;
        this.takeBackImage = takeBackImage;
    }
}

package com.harusari.chainware.takeback.query.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class TakeBackProductInfo {

    private Long productId;
    private String productCode;
    private String productName;
    private String unitQuantity;
    private String unitSpec;
    private int quantity;
    private int price;
    private String takeBackReason;
    private String takeBackImage;

    @QueryProjection
    public TakeBackProductInfo(
            Long productId, String productCode, String productName, String unitQuantity,
            String unitSpec, int quantity, int price, String takeBackReason, String takeBackImage
    ) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.unitQuantity = unitQuantity;
        this.unitSpec = unitSpec;
        this.quantity = quantity;
        this.price = price;
        this.takeBackReason = takeBackReason;
        this.takeBackImage = takeBackImage;
    }

}
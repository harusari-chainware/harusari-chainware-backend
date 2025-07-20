package com.harusari.chainware.delivery.query.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class DeliveryTakeBackProductInfo {
    private String productCode;
    private String productName;
    private String unitQuantity;
    private String unitSpec;
    private String storeType;
    private int quantity;
    private int price;
    private String takeBackReason;
    private String takeBackImage;

    @QueryProjection
    public DeliveryTakeBackProductInfo(
            String productCode, String productName,
            String unitQuantity, String unitSpec, String storeType,
            int quantity, int price, String takeBackReason, String takeBackImage
    ){
        this.productCode=productCode;
        this.productName=productName;
        this.unitQuantity=unitQuantity;
        this.unitSpec=unitSpec;
        this.storeType=storeType;
        this.quantity=quantity;
        this.price=price;
        this.takeBackReason=takeBackReason;
        this.takeBackImage=takeBackImage;
    }
}

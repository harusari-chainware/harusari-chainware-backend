package com.harusari.chainware.takeback.query.dto.response;

import com.harusari.chainware.delivery.query.dto.response.FranchiseInfo;
import com.harusari.chainware.delivery.query.dto.response.WarehouseInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
public class TakeBackDetailResponse {

    private TakeBackBasicInfo takeBackInfo;
    private FranchiseInfo franchiseInfo;
    private WarehouseInfo warehouseInfo;
    private OrderInfo orderInfo;
    private List<TakeBackProductInfo> productInfos;

    @Builder
    @QueryProjection
    public TakeBackDetailResponse(
            TakeBackBasicInfo takeBackInfo,
            FranchiseInfo franchiseInfo,
            WarehouseInfo warehouseInfo,
            OrderInfo orderInfo,
            List<TakeBackProductInfo> productInfos
    ) {
        this.takeBackInfo = takeBackInfo;
        this.franchiseInfo = franchiseInfo;
        this.warehouseInfo = warehouseInfo;
        this.orderInfo = orderInfo;
        this.productInfos = productInfos;
    }

}

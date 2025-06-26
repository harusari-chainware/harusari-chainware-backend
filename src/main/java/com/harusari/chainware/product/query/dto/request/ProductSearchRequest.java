package com.harusari.chainware.product.query.dto.request;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchRequest {
    private String productName;
    private String productCode;
    private Long categoryId;
    private StoreType storeType;
    private String origin;
    private Integer shelfLife;
    private Boolean includeInactive;

    @Builder.Default
    private ProductStatusFilter productStatusFilter = ProductStatusFilter.ACTIVE_ONLY;


    private Integer page;
    private Integer size;

    public int getOffset() {
        return (page != null && size != null) ? (page - 1) * size : 0;
    }

    public int getLimit() {
        return size != null ? size : 10;
    }

    public int getPage() {
        return page != null ? page : 1;
    }

    public int getSize() {
        return size != null ? size : 10;
    }
}
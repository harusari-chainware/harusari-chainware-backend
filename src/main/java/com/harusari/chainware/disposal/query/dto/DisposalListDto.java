package com.harusari.chainware.disposal.query.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DisposalListDto {

    private Long disposalId;
    private String franchiseName;
    private String warehouseName;
    private String productName;
    private String productCode;
    private Integer quantity;
    private String disposalReason;
    private LocalDateTime createdAt;
    private Long takeBackId;

}
package com.harusari.chainware.takeback.query.dto.response;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TakeBackSearchResponse {
    private Long takeBackId;
    private String takeBackCode;
    private TakeBackStatus takeBackStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String warehouseName;
    private String franchiseName;
    private String requesterName;
    private String orderCode;
    private Long orderId;

    @QueryProjection
    public TakeBackSearchResponse(
            Long takeBackId, String takeBackCode, TakeBackStatus takeBackStatus,
            LocalDateTime createdAt, LocalDateTime modifiedAt,
            String warehouseName, String franchiseName, String requesterName,
            String orderCode, Long orderId
    ) {
        this.takeBackId = takeBackId;
        this.takeBackCode = takeBackCode;
        this.takeBackStatus = takeBackStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.warehouseName = warehouseName;
        this.franchiseName = franchiseName;
        this.requesterName = requesterName;
        this.orderCode = orderCode;
        this.orderId = orderId;
    }
}

package com.harusari.chainware.takeback.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TakeBackCreateRequest {

    private Long orderId;
    private List<TakeBackItemRequest> items;

    @Getter
    @Builder
    public static class TakeBackItemRequest {
        private Long orderDetailId;
        private String takeBackReason;
    }

}

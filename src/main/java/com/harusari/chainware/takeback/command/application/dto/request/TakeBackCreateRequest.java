package com.harusari.chainware.takeback.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class TakeBackCreateRequest {

    private Long orderId;
    private List<TakeBackItemRequest> items;

    @Getter
    public static class TakeBackItemRequest {
        private Long orderDetailId;
        private String takeBackReason;
        private String takeBackImage;
    }
}

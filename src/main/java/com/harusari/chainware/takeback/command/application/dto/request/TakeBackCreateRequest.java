package com.harusari.chainware.takeback.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class TakeBackCreateRequest {

    @NotNull
    private Long orderId;

    @NotEmpty
    private List<TakeBackDetailRequest> items;

    @Getter
    public static class TakeBackDetailRequest {
        @NotNull
        private Long productId;

        @NotNull
        private Integer quantity;

        @NotNull
        private Integer price;

        @NotBlank
        private String takeBackReason;

        private String takeBackImage;
    }
}

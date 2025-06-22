package com.harusari.chainware.disposal.command.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DisposalCommandRequestDto {

    private Long productId;
    private Long takeBackId;
    private Integer quantity;
    private String disposalReason;
}

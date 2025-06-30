package com.harusari.chainware.takeback.command.application.dto.response;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class TakeBackCommandResponse {
    private Long takeBackId;
    private TakeBackStatus takeBackStatus;
}

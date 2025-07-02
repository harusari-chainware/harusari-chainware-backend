package com.harusari.chainware.takeback.query.dto.response;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TakeBackBasicInfo {
    private String takeBackCode;
    private TakeBackStatus takeBackStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String rejectReason;
}
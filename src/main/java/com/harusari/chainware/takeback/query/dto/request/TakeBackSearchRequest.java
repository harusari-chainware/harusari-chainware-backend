package com.harusari.chainware.takeback.query.dto.request;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TakeBackSearchRequest {
    private String warehouseName;
    private String warehouseAddress;
    private String franchiseName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private TakeBackStatus takeBackStatus;
}

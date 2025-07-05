package com.harusari.chainware.statistics.query.dto.disposal;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DisposalRateTrendGroupedResponse {
    private List<DisposalRateStatisticsResponse> total;
    private List<DisposalRateStatisticsResponse> headquarters;
    private List<DisposalRateStatisticsResponse> franchises;
}
package com.harusari.chainware.disposal.query.dto;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DisposalListResponseDto {
    private final List<DisposalListDto> items;
    private final Pagination pagination;
}
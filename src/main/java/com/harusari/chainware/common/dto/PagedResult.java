package com.harusari.chainware.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagedResult<T> {
    private List<T> content;
    private PaginationMeta pagination;

    @Getter
    @Builder
    public static class PaginationMeta {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
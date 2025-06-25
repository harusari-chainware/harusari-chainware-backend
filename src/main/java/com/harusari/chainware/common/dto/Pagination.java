package com.harusari.chainware.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pagination {
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;

    public static Pagination of(int page, int size, long totalCount) {
        int totalPages = (int) Math.ceil((double) totalCount / size);
        return Pagination.builder()
                .currentPage(page)
                .totalPages(totalPages)
                .totalItems(totalCount)
                .build();
    }
}
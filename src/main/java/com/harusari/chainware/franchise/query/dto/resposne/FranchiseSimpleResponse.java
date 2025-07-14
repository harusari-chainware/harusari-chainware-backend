package com.harusari.chainware.franchise.query.dto.resposne;

import lombok.Builder;

@Builder
public record FranchiseSimpleResponse(
        Long id, String name
) {
}
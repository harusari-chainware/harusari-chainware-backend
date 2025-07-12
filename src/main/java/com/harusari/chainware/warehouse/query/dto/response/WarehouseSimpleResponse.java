package com.harusari.chainware.warehouse.query.dto.response;

import lombok.Builder;

@Builder
public record WarehouseSimpleResponse(
     Long id, String name
){
}
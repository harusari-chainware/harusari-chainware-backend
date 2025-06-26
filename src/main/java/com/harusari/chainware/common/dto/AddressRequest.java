package com.harusari.chainware.common.dto;

import lombok.Builder;

@Builder
public record AddressRequest(
        String zipcode, String addressRoad, String addressDetail
) {
}
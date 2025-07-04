package com.harusari.chainware.vendor.query.dto;

import lombok.Builder;

@Builder
public record VendorPresignedUrlResponse(
        String presignedUrl
) {
}
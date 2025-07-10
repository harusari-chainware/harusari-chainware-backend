package com.harusari.chainware.vendor.query.dto.response;

import lombok.Builder;

@Builder
public record VendorPresignedUrlResponse(
        String presignedUrl
) {
}
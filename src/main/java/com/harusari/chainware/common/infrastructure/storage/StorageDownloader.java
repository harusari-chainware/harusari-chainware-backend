package com.harusari.chainware.common.infrastructure.storage;

import java.time.Duration;

public interface StorageDownloader {

    String generatePresignedUrl(String s3Key, Duration expireAfter);

}
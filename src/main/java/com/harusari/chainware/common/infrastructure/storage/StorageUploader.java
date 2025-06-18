package com.harusari.chainware.common.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageUploader {

    String uploadAgreement(MultipartFile file, String directory);

}
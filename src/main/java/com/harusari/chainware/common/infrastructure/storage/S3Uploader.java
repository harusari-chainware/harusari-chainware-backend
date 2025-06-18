package com.harusari.chainware.common.infrastructure.storage;

import com.harusari.chainware.exception.common.storage.FileUploadFailedException;
import com.harusari.chainware.exception.common.storage.InvalidFileException;
import com.harusari.chainware.exception.common.storage.StorageErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader implements StorageUploader {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String ALLOWED_FILE_EXTENSION = "pdf";
    private static final String FILE_NAME_DELIMITER = ".";
    private static final String S3_KEY_DELIMITER = "/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    @Override
    public String uploadAgreement(MultipartFile file, String directory) {
        validateFile(file);
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        if (!extension.equals(ALLOWED_FILE_EXTENSION)) {
            throw new InvalidFileException(StorageErrorCode.INVALID_FILE_EXTENSION);
        }

        String uuid = UUID.randomUUID().toString();
        String storedFileName = uuid + FILE_NAME_DELIMITER + extension;
        String s3Key = directory + S3_KEY_DELIMITER + storedFileName;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Key;
        } catch (IOException e) {
            throw new FileUploadFailedException(StorageErrorCode.S3_UPLOAD_FAILED, e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException(StorageErrorCode.FILE_EMPTY);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException(StorageErrorCode.FILE_SIZE_EXCEEDED);
        }
    }

    private String getExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return (index == -1) ? "" : filename.substring(index + 1);
    }

}
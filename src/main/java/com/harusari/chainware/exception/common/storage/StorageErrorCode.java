package com.harusari.chainware.exception.common.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode {

    FILE_EMPTY("11001", "파일이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED("11002", "파일 크기는 최대 10MB까지 가능합니다.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_EXTENSION("11003", "허용되지 않은 파일 확장자입니다.", HttpStatus.BAD_REQUEST),
    S3_UPLOAD_FAILED("11004", "파일 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FRANCHISE_AGREEMENT_NOT_FOUND("11005", "해당 가맹점에 등록된 계약서가 없습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
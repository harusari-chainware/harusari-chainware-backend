package com.harusari.chainware.statistics.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatisticsErrorCode {

    INVALID_PERIOD("50001", "유효하지 않은 기간 형식입니다.", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND("50002", "요청한 통계 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERNAL_ERROR("50003", "통계 처리 중 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PERIOD_NOT_COMPLETED("50004", "선택한 기간은 아직 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_PERIOD("50005", "지원하지 않는 통계 단위입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PERIOD_FOR_FRANCHISE("50006", "가맹점은 월간(MONTHLY) 회전율 조회만 지원합니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}

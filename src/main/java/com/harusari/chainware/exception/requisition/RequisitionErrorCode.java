package com.harusari.chainware.exception.requisition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RequisitionErrorCode {


    REQUISITION_NOT_FOUND("RQ001", "존재하지 않는 품의서입니다.", HttpStatus.NOT_FOUND),
    REQUISITION_ITEM_NOT_FOUND("RQ002", "품의서에 품목이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    REQUISITION_APPROVER_REQUIRED("RQ003", "결재자는 필수입니다.", HttpStatus.BAD_REQUEST),
    REQUISITION_VENDOR_REQUIRED("RQ004", "거래처는 필수입니다.", HttpStatus.BAD_REQUEST),

    REQUISITION_UNAUTHORIZED_WRITER("RQ005", "본인이 작성한 품의서만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    REQUISITION_UNAUTHORIZED_APPROVER("RQ006", "해당 품의서의 결재자가 아닙니다.", HttpStatus.FORBIDDEN),

    REQUISITION_INVALID_STATUS_SUBMIT("RQ007", "임시 저장된 품의서만 상신할 수 있습니다.", HttpStatus.BAD_REQUEST),
    REQUISITION_INVALID_STATUS_APPROVE("RQ008", "SUBMITTED 상태의 품의서만 승인할 수 있습니다.", HttpStatus.BAD_REQUEST),
    REQUISITION_INVALID_STATUS_REJECT("RQ009", "SUBMITTED 상태의 품의서만 반려할 수 있습니다.", HttpStatus.BAD_REQUEST),
    REQUISITION_INVALID_STATUS_DELETE("RQ010", "삭제 가능한 상태가 아닙니다.", HttpStatus.BAD_REQUEST),

    REQUISITION_REJECT_REASON_REQUIRED("RQ011", "반려 사유는 필수입니다.", HttpStatus.BAD_REQUEST),

    REQUISITION_INVALID_STATUS_UPDATE("RQ012", "승인/반려된 품의서는 수정할 수 없습니다.", HttpStatus.BAD_REQUEST);



    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}

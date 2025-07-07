package com.harusari.chainware.requisition.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfo {
    private Long memberId;
    private String name;
    private String position;
    private String email;
}

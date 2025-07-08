package com.harusari.chainware.purchase.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfo {
    private Long memberId;
    private String name;
    private String position;
    private String email;
    private String contact;
}

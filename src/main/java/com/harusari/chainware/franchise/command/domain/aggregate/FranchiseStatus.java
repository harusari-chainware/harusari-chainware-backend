package com.harusari.chainware.franchise.command.domain.aggregate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FranchiseStatus {

    OPERATING("영업중"),
    TEMPORARILY_CLOSED("휴점"),
    PREPARING("준비중"),
    CLOSED("폐점");

    private final String franchiseLabelKo;

}
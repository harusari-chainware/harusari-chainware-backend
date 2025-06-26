package com.harusari.chainware.common.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "address_road")
    private String addressRoad;

    @Column(name = "address_detail")
    private String addressDetail;

}
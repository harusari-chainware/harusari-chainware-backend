package com.harusari.chainware.purchase.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorInfo {
    private Long vendorId;
    private String name;
    private String managerName;
    private String vendorContact;
    private String type;
}

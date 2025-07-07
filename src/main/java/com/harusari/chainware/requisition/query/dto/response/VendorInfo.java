package com.harusari.chainware.requisition.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorInfo {
    private Long vendorId;
    private String name;
    private String managerName;
    private String contact;
    private String type;
}

package com.harusari.chainware.vendor.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "vendor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "vendor_type",
            columnDefinition = "ENUM('SUPPLIER', 'TRUST_CONTRACTOR', 'LOGISTICS', 'AGENCY') COMMENT '업체 유형'"
    )
    private VendorType vendorType;

    @Column(name = "vendor_address")
    private String vendorAddress;

    @Column(name = "vendor_tax_id")
    private String vendorTaxId;

    @Column(name = "vendor_memo")
    private String vendorMemo;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "vendor_status",
            columnDefinition = "ENUM('ACTIVE', 'EXPIRED') COMMENT '계약 상태'"
    )
    private VendorStatus vendorStatus;

    @Column(name = "agreement")
    private String agreement;

    @Column(name = "vendor_start_date")
    private LocalDate vendorStartDate;

    @Column(name = "vendor_end_date")
    private LocalDate vendorEndDate;

    @Builder
    public Vendor(
            Long memberId, String vendorName, VendorType vendorType, String vendorAddress,
            String vendorTaxId, String vendorMemo, VendorStatus vendorStatus,
            String agreement, LocalDate vendorStartDate, LocalDate vendorEndDate
    ){
        this.memberId = memberId;
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
        this.vendorTaxId = vendorTaxId;
        this.vendorMemo = vendorMemo;
        this.vendorStatus = vendorStatus;
        this.agreement = agreement;
        this.vendorStartDate = vendorStartDate;
        this.vendorEndDate = vendorEndDate;
    }

    public void update(
            String vendorName, VendorType vendorType, String vendorAddress,
            String vendorTaxId, String vendorMemo, VendorStatus vendorStatus,
            String agreement, LocalDate vendorStartDate, LocalDate vendorEndDate
    ) {
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
        this.vendorTaxId = vendorTaxId;
        this.vendorMemo = vendorMemo;
        this.vendorStatus = vendorStatus;
        this.agreement = agreement;
        this.vendorStartDate = vendorStartDate;
        this.vendorEndDate = vendorEndDate;
    }

    public void changeStatus(VendorStatus newStatus) {
        this.vendorStatus = newStatus;
    }

}
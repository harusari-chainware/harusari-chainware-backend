package com.harusari.chainware.vendor.command.domain.aggregate;

import com.harusari.chainware.common.domain.vo.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "vendor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_type")
    private VendorType vendorType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "vendor_zipcode")),
            @AttributeOverride(name = "addressRoad", column = @Column(name = "vendor_address_road")),
            @AttributeOverride(name = "addressDetail", column = @Column(name = "vendor_address_detail"))
    })
    private Address vendorAddress;

    @Column(name = "vendor_tax_id")
    private String vendorTaxId;

    @Column(name = "vendor_memo")
    private String vendorMemo;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_status")
    private VendorStatus vendorStatus;

    @Column(name = "agreement_file_path")
    private String agreementFilePath;

    @Column(name = "agreement_original_file_name")
    private String agreementOriginalFileName;

    @Column(name = "agreement_file_size")
    private long agreementFileSize;

    @Column(name = "agreement_uploaded_at")
    private LocalDateTime agreementUploadedAt;

    @Column(name = "vendor_start_date")
    private LocalDate vendorStartDate;

    @Column(name = "vendor_end_date")
    private LocalDate vendorEndDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public Vendor(
            Long memberId, String vendorName, VendorType vendorType, Address vendorAddress,
            String vendorTaxId, String vendorMemo, VendorStatus vendorStatus, String agreementFilePath,
            String agreementOriginalFileName, long agreementFileSize, LocalDateTime agreementUploadedAt,
            LocalDate vendorStartDate, LocalDate vendorEndDate
    ) {
        this.memberId = memberId;
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
        this.vendorTaxId = vendorTaxId;
        this.vendorMemo = vendorMemo;
        this.vendorStatus = vendorStatus;
        this.agreementFilePath = agreementFilePath;
        this.agreementOriginalFileName = agreementOriginalFileName;
        this.agreementFileSize = agreementFileSize;
        this.agreementUploadedAt = agreementUploadedAt;
        this.vendorStartDate = vendorStartDate;
        this.vendorEndDate = vendorEndDate;
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    public void updateVendor(
            String vendorName, VendorType vendorType, String vendorTaxId,
            Address vendorAddress, String vendorMemo, VendorStatus vendorStatus,
            LocalDate vendorStartDate, LocalDate vendorEndDate
    ) {
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorTaxId = vendorTaxId;
        this.vendorAddress = vendorAddress;
        this.vendorMemo = vendorMemo;
        this.vendorStatus = vendorStatus;
        this.vendorStartDate = vendorStartDate;
        this.vendorEndDate = vendorEndDate;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void updateAgreementInfo(
            String filePath, String originalFilename,
            long size, LocalDateTime localDateTime
    ) {
        this.agreementFilePath = filePath;
        this.agreementOriginalFileName = originalFilename;
        this.agreementFileSize = size;
        this.agreementUploadedAt = localDateTime;
    }

}
package com.harusari.chainware.franchise.command.domain.aggregate;

import com.harusari.chainware.common.domain.vo.Address;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "franchise")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Franchise {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "franchise_id")
    private Long franchiseId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "franchise_name")
    private String franchiseName;

    @Column(name = "franchise_contact")
    private String franchiseContact;

    @Column(name = "franchise_tax_id")
    private String franchiseTaxId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "franchise_zipcode")),
            @AttributeOverride(name = "addressRoad", column = @Column(name = "franchise_address_road")),
            @AttributeOverride(name = "addressDetail", column = @Column(name = "franchise_address_detail"))
    })
    private Address franchiseAddress;

    @Column(name = "agreement_file_path")
    private String agreementFilePath;

    @Column(name = "agreement_original_file_name")
    private String agreementOriginalFileName;

    @Column(name = "agreement_file_size")
    private long agreementFileSize;

    @Column(name = "agreement_uploaded_at")
    private LocalDateTime agreementUploadedAt;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "franchise_status")
    @Enumerated(EnumType.STRING)
    private FranchiseStatus franchiseStatus = FranchiseStatus.ACTIVE;

    @Builder
    public Franchise(
            Long memberId, String franchiseName, String franchiseContact, String franchiseTaxId,
            Address franchiseAddress, String agreementFilePath, String agreementOriginalFileName,
            long agreementFileSize, LocalDate contractStartDate, LocalDate contractEndDate, LocalDateTime modifiedAt
    ) {
        this.memberId = memberId;
        this.franchiseName = franchiseName;
        this.franchiseContact = franchiseContact;
        this.franchiseTaxId = franchiseTaxId;
        this.franchiseAddress = franchiseAddress;
        this.agreementFilePath = agreementFilePath;
        this.agreementOriginalFileName = agreementOriginalFileName;
        this.agreementFileSize = agreementFileSize;
        this.agreementUploadedAt = LocalDateTime.now().withNano(0);
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.createdAt = LocalDateTime.now().withNano(0);
        this.modifiedAt = modifiedAt;
    }

    public void updateFranchise(
            String franchiseName, String franchiseContact,
            String franchiseTaxId, Address franchiseAddress
    ) {
        this.franchiseName = franchiseName;
        this.franchiseContact = franchiseContact;
        this.franchiseTaxId = franchiseTaxId;
        this.franchiseAddress = franchiseAddress;
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
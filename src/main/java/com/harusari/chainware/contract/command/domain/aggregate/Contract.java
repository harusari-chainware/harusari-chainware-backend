package com.harusari.chainware.contract.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "contract_price")
    private Integer contractPrice;

    @Column(name = "min_order_qty")
    private Integer minOrderQty;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status")
    private ContractStatus contractStatus;

    @Builder
    public Contract(Long productId, Long vendorId, Integer contractPrice,
                    Integer minOrderQty, Integer leadTime,
                    LocalDate contractStartDate, LocalDate contractEndDate,
                    ContractStatus contractStatus) {
        this.productId = productId;
        this.vendorId = vendorId;
        this.contractPrice = contractPrice;
        this.minOrderQty = minOrderQty;
        this.leadTime = leadTime;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.contractStatus = contractStatus;
        this.isDeleted = false;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void updateContract(Integer contractPrice, Integer minOrderQty, Integer leadTime, LocalDate startDate, LocalDate endDate, ContractStatus status) {
        this.contractPrice = contractPrice;
        this.minOrderQty = minOrderQty;
        this.leadTime = leadTime;
        this.contractStartDate = startDate;
        this.contractEndDate = endDate;
        this.contractStatus = status;
    }

    public void updateContractFields(
            Long productId,
            Long vendorId,
            Integer contractPrice,
            Integer minOrderQty,
            Integer leadTime,
            LocalDate contractStartDate,
            LocalDate contractEndDate,
            ContractStatus contractStatus
    ) {
        this.productId = productId;
        this.vendorId = vendorId;
        this.contractPrice = contractPrice;
        this.minOrderQty = minOrderQty;
        this.leadTime = leadTime;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.contractStatus = contractStatus;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }
}
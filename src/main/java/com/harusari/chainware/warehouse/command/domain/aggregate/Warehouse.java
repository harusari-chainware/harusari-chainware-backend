package com.harusari.chainware.warehouse.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "warehouse")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Warehouse {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "warehouse_address")
    private String warehouseAddress;

    @Column(name = "warehouse_status")
    private boolean warehouseStatus = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Warehouse(
            Long memberId, String warehouseName, String warehouseAddress,
            boolean warehouseStatus, LocalDateTime modifiedAt, boolean isDeleted
    ) {
        this.memberId = memberId;
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        this.warehouseStatus = warehouseStatus;
        this.createdAt = LocalDateTime.now().withNano(0);
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
    }

}
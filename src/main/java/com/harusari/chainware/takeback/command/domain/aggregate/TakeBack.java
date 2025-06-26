package com.harusari.chainware.takeback.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "take_back")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TakeBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "take_back_id")
    private Long takeBackId;

    @Column(name = "store_order_id", nullable = false)
    private Long orderId;

    @Column(name = "take_back_code", nullable = false)
    private String takeBackCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "take_back_status", nullable = false)
    private TakeBackStatus takeBackStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public TakeBack(Long orderId, String takeBackCode) {
        this.orderId = orderId;
        this.takeBackCode = takeBackCode;
        this.takeBackStatus = TakeBackStatus.REQUESTED;
        this.createdAt = LocalDateTime.now();
    }

    public void cancel() {
        this.takeBackStatus = TakeBackStatus.CANCELLED;
        this.modifiedAt = LocalDateTime.now();
    }

    public void collect() {
        this.takeBackStatus = TakeBackStatus.COLLECTED;
        this.modifiedAt = LocalDateTime.now();
    }

    public void approve() {
        this.takeBackStatus = TakeBackStatus.APPROVED;
        this.modifiedAt = LocalDateTime.now();
    }

    public void reject() {
        this.takeBackStatus = TakeBackStatus.REJECTED;
        this.modifiedAt = LocalDateTime.now();
    }
}

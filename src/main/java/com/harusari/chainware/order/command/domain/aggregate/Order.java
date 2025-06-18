package com.harusari.chainware.order.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "store_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_id")
    private Long orderId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
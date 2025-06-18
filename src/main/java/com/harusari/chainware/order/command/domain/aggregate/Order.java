package com.harusari.chainware.order.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @Column(name = "franchise_id", nullable = false)
    private Long franchiseId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "store_order_code", nullable = false, length = 50)
    private String orderCode;

    @Column(name = "delivery_due_date", nullable = false)
    private LocalDate deliveryDueDate;

    @Column(name = "product_count", nullable = false)
    private Integer productCount;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Column(name = "store_order_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

}
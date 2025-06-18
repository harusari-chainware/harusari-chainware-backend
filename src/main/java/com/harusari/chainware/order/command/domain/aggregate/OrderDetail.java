package com.harusari.chainware.order.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "store_order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_detail_id")
    private Long orderDetailId;

    @Column(name = "store_order_id")
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}

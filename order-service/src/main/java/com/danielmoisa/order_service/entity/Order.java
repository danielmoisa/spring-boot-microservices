package com.danielmoisa.order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "quantity")
    private Long ticketCount;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @CreationTimestamp
    @Column(name = "placed_at", nullable = false, updatable = false)
    private LocalDateTime placedAt;

    @Column(name = "customer_id")
    private Long customerId;
}

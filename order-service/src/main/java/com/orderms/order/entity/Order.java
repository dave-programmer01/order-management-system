package com.orderms.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// JPA entity representing a customer order
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email of the user who placed the order
    private String userEmail;

    // ID of the product being ordered
    private Long productId;

    // Number of items ordered
    private Integer quantity;

    // Total cost (price * quantity)
    private BigDecimal totalPrice;

    // Current status of the order
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // When the order was placed
    private LocalDateTime orderDate;
}
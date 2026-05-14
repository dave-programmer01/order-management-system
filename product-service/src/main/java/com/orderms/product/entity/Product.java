package com.orderms.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Display name for the product
    private String name;

    // Optional longer description
    private String description;

    // Unit price stored as BigDecimal for precision
    private BigDecimal price;

    // How many units are in stock
    private Integer stockQuantity;
}
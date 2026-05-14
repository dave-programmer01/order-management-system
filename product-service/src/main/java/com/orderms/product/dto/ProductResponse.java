package com.orderms.product.dto;

import java.math.BigDecimal;

// Outgoing response shape sent back to clients
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity
) {}
package com.orderms.order.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Outgoing response payload representing a created order
@Builder
public record OrderResponse(
        Long id,
        String userEmail,
        Long productId,
        Integer quantity,
        BigDecimal totalPrice,
        String status,
        LocalDateTime orderDate
) {}
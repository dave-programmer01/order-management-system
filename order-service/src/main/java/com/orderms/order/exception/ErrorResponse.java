package com.orderms.order.exception;

import java.time.LocalDateTime;

// Standard error response shape for all exception handlers
public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {}
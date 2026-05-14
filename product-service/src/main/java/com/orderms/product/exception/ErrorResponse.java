package com.orderms.product.exception;

import java.time.LocalDateTime;

// Standard error shape returned to clients
public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
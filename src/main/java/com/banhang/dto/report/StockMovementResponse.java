package com.banhang.dto.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockMovementResponse(
    Long id,
    LocalDateTime occurredAt,
    Integer variantId,
    Integer productId,
    String productName,
    String movementType,
    int quantityDelta,
    BigDecimal unitCost,
    String reference,
    String note) {}

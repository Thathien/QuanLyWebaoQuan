package com.banhang.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record InventoryAdjustmentRequest(
    @NotNull @Positive Integer variantId,
    int quantityDelta,
    BigDecimal unitCost,
    @NotBlank String reason) {}

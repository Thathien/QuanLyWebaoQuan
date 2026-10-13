package com.banhang.dto.report;

import java.math.BigDecimal;

public record InventoryVariantResponse(
    Integer variantId,
    String productName,
    String size,
    String color,
    int quantityOnHand,
    BigDecimal averageUnitCost,
    boolean canSetOpeningBalance) {}

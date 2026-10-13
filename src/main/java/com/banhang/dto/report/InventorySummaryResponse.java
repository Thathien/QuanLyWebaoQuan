package com.banhang.dto.report;

import java.math.BigDecimal;
import java.util.List;

public record InventorySummaryResponse(
    long variantCount,
    long totalQuantity,
    long zeroStockVariantCount,
    long lowStockVariantCount,
    BigDecimal inventoryValue,
    boolean inventoryValueComplete,
    List<InventoryProductResponse> products) {}

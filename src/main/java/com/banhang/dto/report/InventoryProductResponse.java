package com.banhang.dto.report;

import java.math.BigDecimal;

public record InventoryProductResponse(
    Integer productId,
    String productName,
    Integer categoryId,
    String categoryName,
    long quantityOnHand,
    long minimumStock,
    BigDecimal inventoryValue,
    boolean costAvailable) {}

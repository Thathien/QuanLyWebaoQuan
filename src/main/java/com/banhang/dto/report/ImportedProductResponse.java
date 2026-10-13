package com.banhang.dto.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ImportedProductResponse(
    Integer productId,
    Integer productCode,
    String productName,
    Integer categoryId,
    String categoryName,
    long quantityImported,
    BigDecimal totalImportValue,
    long receiptCount,
    BigDecimal averageUnitCost,
    LocalDateTime lastImportedAt) {}

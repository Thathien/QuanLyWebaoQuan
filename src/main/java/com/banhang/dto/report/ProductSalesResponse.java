package com.banhang.dto.report;

import java.math.BigDecimal;

public record ProductSalesResponse(
    Integer productId,
    String productCode,
    String productName,
    Integer categoryId,
    String categoryName,
    long quantitySold,
    BigDecimal grossRevenue,
    BigDecimal refunds,
    BigDecimal netRevenue,
    BigDecimal averageSellingPrice,
    BigDecimal costOfGoodsSold,
    BigDecimal grossProfit,
    BigDecimal revenueSharePercent) {}

package com.banhang.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RevenueSummaryResponse(
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal grossRevenue,
    BigDecimal refunds,
    BigDecimal netRevenue,
    long completedOrders,
    long productsSold,
    BigDecimal averageOrderValue,
    BigDecimal costOfGoodsSold,
    BigDecimal grossProfit,
    BigDecimal grossMarginPercent,
    BigDecimal previousPeriodRevenue,
    BigDecimal revenueChangePercent,
    List<RevenueBucketResponse> series) {}

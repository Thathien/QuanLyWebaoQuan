package com.banhang.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueBucketResponse(
    LocalDate periodStart,
    BigDecimal grossRevenue,
    BigDecimal refunds,
    BigDecimal netRevenue,
    long completedOrders,
    long productsSold) {}

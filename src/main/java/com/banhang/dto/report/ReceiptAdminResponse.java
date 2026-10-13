package com.banhang.dto.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReceiptAdminResponse(
    Long id,
    Long supplierId,
    String supplierName,
    String status,
    LocalDateTime createdAt,
    LocalDateTime confirmedAt,
    List<ReceiptLine> items) {
  public record ReceiptLine(
      Integer variantId,
      String productName,
      String size,
      String color,
      int quantity,
      BigDecimal unitCost) {}
}

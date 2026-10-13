package com.banhang.dto.report;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public record PhieuNhapRequest(
    @NotNull @Positive Long supplierId, @NotEmpty List<@Valid ReceiptLine> items) {
  public record ReceiptLine(
      @NotNull @Positive Integer variantId,
      @Positive int quantity,
      @NotNull @PositiveOrZero BigDecimal unitCost) {}
}

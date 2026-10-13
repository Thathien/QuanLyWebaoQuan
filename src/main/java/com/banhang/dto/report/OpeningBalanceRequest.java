package com.banhang.dto.report;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public record OpeningBalanceRequest(@NotEmpty List<@Valid OpeningLine> items) {
  public record OpeningLine(
      @NotNull @Positive Integer variantId,
      @PositiveOrZero int quantity,
      @NotNull @PositiveOrZero BigDecimal unitCost) {}
}

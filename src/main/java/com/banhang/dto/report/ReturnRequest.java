package com.banhang.dto.report;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record ReturnRequest(@NotBlank String reason, @NotEmpty List<@Valid ReturnLine> items) {
  public record ReturnLine(@NotNull @Positive Integer variantId, @Positive int quantity) {}
}

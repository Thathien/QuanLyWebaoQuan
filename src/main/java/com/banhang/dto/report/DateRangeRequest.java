package com.banhang.dto.report;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record DateRangeRequest(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {}

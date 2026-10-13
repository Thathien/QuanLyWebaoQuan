package com.banhang.dto.report;

import java.time.LocalDateTime;

public record PhieuNhapResponse(
    Long id, Long supplierId, String status, LocalDateTime confirmedAt) {}

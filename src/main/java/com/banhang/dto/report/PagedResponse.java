package com.banhang.dto.report;

import java.util.List;

public record PagedResponse<T>(List<T> items, int page, int size, boolean hasNext) {}

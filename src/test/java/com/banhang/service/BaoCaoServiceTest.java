package com.banhang.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.banhang.dao.BaoCaoDao;
import com.banhang.entity.TrangThaiHoaDon;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BaoCaoServiceTest {
  @Mock private BaoCaoDao baoCaoDao;
  @InjectMocks private BaoCaoService baoCaoService;

  @Test
  void revenueSummarySubtractsRefundsAndReturnedGoodsCost() {
    LocalDate start = LocalDate.of(2026, 1, 5);
    LocalDate end = LocalDate.of(2026, 1, 6);
    when(baoCaoDao.revenueSummary(
            start.atStartOfDay(),
            end.plusDays(1).atStartOfDay(),
            TrangThaiHoaDon.HOAN_THANH.getCode()))
        .thenReturn(
            new Object[] {
              BigDecimal.valueOf(1000),
              2L,
              4L,
              BigDecimal.valueOf(500),
              0L,
              BigDecimal.valueOf(100),
              1L,
              BigDecimal.valueOf(50),
              0L
            });
    when(baoCaoDao.revenueSummary(
            start.minusDays(2).atStartOfDay(),
            start.atStartOfDay(),
            TrangThaiHoaDon.HOAN_THANH.getCode()))
        .thenReturn(
            new Object[] {
              BigDecimal.valueOf(600),
              1L,
              2L,
              BigDecimal.valueOf(200),
              0L,
              BigDecimal.valueOf(100),
              1L,
              BigDecimal.ZERO,
              0L
            });
    when(baoCaoDao.revenueSeries(
            start.atStartOfDay(),
            end.plusDays(1).atStartOfDay(),
            "day",
            TrangThaiHoaDon.HOAN_THANH.getCode()))
        .thenReturn(List.of());

    var result = baoCaoService.revenueSummary(start, end, "day");

    assertEquals(BigDecimal.valueOf(900), result.netRevenue());
    assertEquals(BigDecimal.valueOf(450), result.costOfGoodsSold());
    assertEquals(BigDecimal.valueOf(450), result.grossProfit());
    assertEquals(new BigDecimal("50.00"), result.grossMarginPercent());
    assertEquals(new BigDecimal("450.00"), result.averageOrderValue());
    assertEquals(BigDecimal.valueOf(500), result.previousPeriodRevenue());
    assertEquals(new BigDecimal("80.00"), result.revenueChangePercent());
  }
}

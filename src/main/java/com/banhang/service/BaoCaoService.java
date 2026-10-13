package com.banhang.service;

import com.banhang.dao.BaoCaoDao;
import com.banhang.dto.report.ImportedProductResponse;
import com.banhang.dto.report.InventoryProductResponse;
import com.banhang.dto.report.InventorySummaryResponse;
import com.banhang.dto.report.PagedResponse;
import com.banhang.dto.report.ProductSalesResponse;
import com.banhang.dto.report.RevenueBucketResponse;
import com.banhang.dto.report.RevenueSummaryResponse;
import com.banhang.dto.report.StockMovementResponse;
import com.banhang.entity.TrangThaiHoaDon;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BaoCaoService {
  private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
  private final BaoCaoDao baoCaoDao;

  public BaoCaoService(BaoCaoDao baoCaoDao) {
    this.baoCaoDao = baoCaoDao;
  }

  public RevenueSummaryResponse revenueSummary(
      LocalDate startDate, LocalDate endDate, String seriesGranularity) {
    DateBounds current = bounds(startDate, endDate);
    long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
    DateBounds previous = new DateBounds(current.start().minusDays(days), current.start());
    RevenueValues values = revenueValues(current);
    RevenueValues previousValues = revenueValues(previous);
    List<RevenueBucketResponse> series =
        baoCaoDao
            .revenueSeries(
                current.start(),
                current.endExclusive(),
                seriesGranularity,
                TrangThaiHoaDon.HOAN_THANH.getCode())
            .stream()
            .map(this::toBucket)
            .toList();

    BigDecimal change =
        previousValues.netRevenue().signum() == 0
            ? null
            : values
                .netRevenue()
                .subtract(previousValues.netRevenue())
                .multiply(ONE_HUNDRED)
                .divide(previousValues.netRevenue().abs(), 2, RoundingMode.HALF_UP);
    BigDecimal profit =
        values.costOfGoodsSold() == null
            ? null
            : values.netRevenue().subtract(values.costOfGoodsSold());
    BigDecimal margin =
        profit == null || values.netRevenue().signum() <= 0
            ? null
            : profit.multiply(ONE_HUNDRED).divide(values.netRevenue(), 2, RoundingMode.HALF_UP);
    BigDecimal averageOrder =
        values.completedOrders() == 0
            ? BigDecimal.ZERO
            : values
                .netRevenue()
                .divide(BigDecimal.valueOf(values.completedOrders()), 2, RoundingMode.HALF_UP);

    return new RevenueSummaryResponse(
        startDate,
        endDate,
        values.grossRevenue(),
        values.refunds(),
        values.netRevenue(),
        values.completedOrders(),
        values.productsSold(),
        averageOrder,
        values.costOfGoodsSold(),
        profit,
        margin,
        previousValues.netRevenue(),
        change,
        series);
  }

  public PagedResponse<ProductSalesResponse> bestSelling(
      LocalDate startDate,
      LocalDate endDate,
      Integer categoryId,
      String search,
      String sortBy,
      int page,
      int size) {
    DateBounds bounds = bounds(startDate, endDate);
    BigDecimal totalRevenue = revenueValues(bounds).netRevenue();
    List<Object[]> raw =
        baoCaoDao.bestSelling(
            bounds.start(),
            bounds.endExclusive(),
            categoryId,
            search,
            sortBy,
            TrangThaiHoaDon.HOAN_THANH.getCode(),
            page * size,
            size + 1);
    boolean hasNext = raw.size() > size;
    List<ProductSalesResponse> items = new ArrayList<>();
    for (Object[] row : raw.stream().limit(size).toList()) {
      BigDecimal grossRevenue = decimal(row[6]);
      BigDecimal refunds = decimal(row[7]);
      BigDecimal netRevenue = decimal(row[8]);
      BigDecimal cogs = row[10] == null ? null : decimal(row[10]);
      items.add(
          new ProductSalesResponse(
              integer(row[0]),
              string(row[1]),
              string(row[2]),
              integer(row[3]),
              string(row[4]),
              number(row[5]),
              grossRevenue,
              refunds,
              netRevenue,
              decimal(row[9]),
              cogs,
              cogs == null ? null : netRevenue.subtract(cogs),
              totalRevenue.signum() <= 0
                  ? null
                  : netRevenue
                      .multiply(ONE_HUNDRED)
                      .divide(totalRevenue, 2, RoundingMode.HALF_UP)));
    }
    return new PagedResponse<>(items, page, size, hasNext);
  }

  public PagedResponse<ImportedProductResponse> mostImported(
      LocalDate startDate,
      LocalDate endDate,
      Integer categoryId,
      Long supplierId,
      String search,
      int page,
      int size) {
    DateBounds bounds = bounds(startDate, endDate);
    List<Object[]> raw =
        baoCaoDao.mostImported(
            bounds.start(),
            bounds.endExclusive(),
            categoryId,
            supplierId,
            search,
            page * size,
            size + 1);
    boolean hasNext = raw.size() > size;
    List<ImportedProductResponse> items = new ArrayList<>();
    for (Object[] row : raw.stream().limit(size).toList()) {
      items.add(
          new ImportedProductResponse(
              integer(row[0]),
              integer(row[0]),
              string(row[1]),
              integer(row[2]),
              string(row[3]),
              number(row[4]),
              decimal(row[5]),
              number(row[6]),
              decimal(row[7]),
              localDateTime(row[8])));
    }
    return new PagedResponse<>(items, page, size, hasNext);
  }

  public InventorySummaryResponse inventorySummary(
      Integer categoryId, Boolean zeroStock, Boolean lowStock, int page, int size) {
    Object[] summary = baoCaoDao.inventorySummary();
    List<Object[]> raw =
        baoCaoDao.inventoryProducts(categoryId, zeroStock, lowStock, page * size, size + 1);
    List<InventoryProductResponse> products = toInventoryProducts(raw, size);
    return new InventorySummaryResponse(
        number(summary[0]),
        number(summary[1]),
        number(summary[2]),
        number(summary[3]),
        number(summary[4]) == 0 && summary[5] != null ? decimal(summary[5]) : null,
        number(summary[4]) == 0,
        products);
  }

  public PagedResponse<InventoryProductResponse> lowStock(Integer categoryId, int page, int size) {
    List<Object[]> raw = baoCaoDao.inventoryProducts(categoryId, null, true, page * size, size + 1);
    boolean hasNext = raw.size() > size;
    return new PagedResponse<>(toInventoryProducts(raw, size), page, size, hasNext);
  }

  public PagedResponse<InventoryProductResponse> slowMoving(
      LocalDate startDate,
      LocalDate endDate,
      Integer categoryId,
      String search,
      int page,
      int size) {
    DateBounds bounds = bounds(startDate, endDate);
    List<Object[]> raw =
        baoCaoDao.slowMoving(
            bounds.start(), bounds.endExclusive(), categoryId, search, page * size, size + 1);
    boolean hasNext = raw.size() > size;
    List<InventoryProductResponse> items = new ArrayList<>();
    for (Object[] row : raw.stream().limit(size).toList()) {
      items.add(
          new InventoryProductResponse(
              integer(row[0]),
              string(row[1]),
              integer(row[2]),
              string(row[3]),
              number(row[4]),
              0,
              null,
              number(row[5]) == 0));
    }
    return new PagedResponse<>(items, page, size, hasNext);
  }

  public PagedResponse<StockMovementResponse> stockMovements(
      LocalDate startDate, LocalDate endDate, Integer variantId, int page, int size) {
    LocalDate start = startDate == null ? LocalDate.of(1900, 1, 1) : startDate;
    LocalDate end = endDate == null ? LocalDate.now() : endDate;
    DateBounds bounds = bounds(start, end);
    List<Object[]> raw =
        baoCaoDao.stockMovements(
            bounds.start(), bounds.endExclusive(), variantId, page * size, size + 1);
    boolean hasNext = raw.size() > size;
    List<StockMovementResponse> items =
        raw.stream()
            .limit(size)
            .map(
                row ->
                    new StockMovementResponse(
                        number(row[0]),
                        localDateTime(row[1]),
                        integer(row[2]),
                        integer(row[3]),
                        string(row[4]),
                        string(row[5]),
                        (int) number(row[6]),
                        row[7] == null ? null : decimal(row[7]),
                        string(row[8]),
                        string(row[9])))
            .toList();
    return new PagedResponse<>(items, page, size, hasNext);
  }

  private RevenueValues revenueValues(DateBounds bounds) {
    Object[] row =
        baoCaoDao.revenueSummary(
            bounds.start(), bounds.endExclusive(), TrangThaiHoaDon.HOAN_THANH.getCode());
    BigDecimal gross = decimal(row[0]);
    BigDecimal refunds = decimal(row[5]);
    BigDecimal net = gross.subtract(refunds);
    boolean costsKnown = number(row[4]) == 0 && number(row[8]) == 0 && row[3] != null;
    BigDecimal cogs = costsKnown ? decimal(row[3]).subtract(decimal(row[7])) : null;
    return new RevenueValues(gross, refunds, net, number(row[1]), number(row[2]), cogs);
  }

  private List<InventoryProductResponse> toInventoryProducts(List<Object[]> raw, int limit) {
    List<InventoryProductResponse> products = new ArrayList<>();
    for (Object[] row : raw.stream().limit(limit).toList()) {
      boolean costAvailable = number(row[7]) == 0;
      products.add(
          new InventoryProductResponse(
              integer(row[0]),
              string(row[1]),
              integer(row[2]),
              string(row[3]),
              number(row[4]),
              number(row[5]),
              costAvailable && row[6] != null ? decimal(row[6]) : null,
              costAvailable));
    }
    return products;
  }

  private RevenueBucketResponse toBucket(Object[] row) {
    BigDecimal gross = decimal(row[1]);
    BigDecimal refunds = decimal(row[2]);
    return new RevenueBucketResponse(
        localDate(row[0]), gross, refunds, gross.subtract(refunds), number(row[3]), number(row[4]));
  }

  private DateBounds bounds(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("Khoảng ngày không hợp lệ.");
    }
    return new DateBounds(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
  }

  private BigDecimal decimal(Object value) {
    if (value == null) {
      return BigDecimal.ZERO;
    }
    if (value instanceof BigDecimal decimal) {
      return decimal;
    }
    return new BigDecimal(value.toString());
  }

  private long number(Object value) {
    return value == null ? 0 : ((Number) value).longValue();
  }

  private Integer integer(Object value) {
    return value == null ? null : ((Number) value).intValue();
  }

  private String string(Object value) {
    return value == null ? null : value.toString();
  }

  private LocalDate localDate(Object value) {
    if (value instanceof LocalDate date) {
      return date;
    }
    if (value instanceof Date date) {
      return date.toLocalDate();
    }
    return LocalDate.parse(value.toString());
  }

  private LocalDateTime localDateTime(Object value) {
    if (value instanceof LocalDateTime dateTime) {
      return dateTime;
    }
    if (value instanceof java.sql.Timestamp timestamp) {
      return timestamp.toLocalDateTime();
    }
    return value == null ? null : LocalDateTime.parse(value.toString().replace(' ', 'T'));
  }

  private record DateBounds(LocalDateTime start, LocalDateTime endExclusive) {}

  private record RevenueValues(
      BigDecimal grossRevenue,
      BigDecimal refunds,
      BigDecimal netRevenue,
      long completedOrders,
      long productsSold,
      BigDecimal costOfGoodsSold) {}
}

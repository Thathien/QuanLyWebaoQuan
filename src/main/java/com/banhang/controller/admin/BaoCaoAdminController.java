package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.dto.report.ImportedProductResponse;
import com.banhang.dto.report.InventoryProductResponse;
import com.banhang.dto.report.InventorySummaryResponse;
import com.banhang.dto.report.PagedResponse;
import com.banhang.dto.report.ProductSalesResponse;
import com.banhang.dto.report.RevenueBucketResponse;
import com.banhang.dto.report.RevenueSummaryResponse;
import com.banhang.dto.report.StockMovementResponse;
import com.banhang.service.BaoCaoService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
public class BaoCaoAdminController {
  private static final int MAX_PAGE_SIZE = 100;
  private final BaoCaoService baoCaoService;

  public BaoCaoAdminController(BaoCaoService baoCaoService) {
    this.baoCaoService = baoCaoService;
  }

  @GetMapping("/revenue/summary")
  public RevenueSummaryResponse revenueSummary(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer quarter,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, month, quarter);
    String granularity = range.days() <= 31 ? "day" : range.days() <= 120 ? "month" : "month";
    return baoCaoService.revenueSummary(range.start(), range.end(), granularity);
  }

  @GetMapping("/revenue/by-day")
  public List<RevenueBucketResponse> revenueByDay(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, month, null);
    return baoCaoService.revenueSummary(range.start(), range.end(), "day").series();
  }

  @GetMapping("/revenue/by-month")
  public List<RevenueBucketResponse> revenueByMonth(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, null, null);
    return baoCaoService.revenueSummary(range.start(), range.end(), "month").series();
  }

  @GetMapping("/revenue/by-quarter")
  public List<RevenueBucketResponse> revenueByQuarter(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, null, null);
    return baoCaoService.revenueSummary(range.start(), range.end(), "quarter").series();
  }

  @GetMapping("/revenue/by-year")
  public List<RevenueBucketResponse> revenueByYear(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, null, null, null);
    return baoCaoService.revenueSummary(range.start(), range.end(), "year").series();
  }

  @GetMapping("/products/best-selling")
  public PagedResponse<ProductSalesResponse> bestSelling(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer quarter,
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "revenue") String sortBy,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, month, quarter);
    validatePaging(page, size);
    if (!"revenue".equals(sortBy) && !"quantity".equals(sortBy)) {
      throw new IllegalArgumentException("sortBy chỉ nhận revenue hoặc quantity.");
    }
    return baoCaoService.bestSelling(
        range.start(), range.end(), categoryId, search, sortBy, page, size);
  }

  @GetMapping("/products/most-imported")
  public PagedResponse<ImportedProductResponse> mostImported(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer quarter,
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) Long supplierId,
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, month, quarter);
    validatePaging(page, size);
    return baoCaoService.mostImported(
        range.start(), range.end(), categoryId, supplierId, search, page, size);
  }

  @GetMapping("/inventory/summary")
  public InventorySummaryResponse inventorySummary(
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) Boolean zeroStock,
      @RequestParam(required = false) Boolean lowStock,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      HttpSession session) {
    requireAdmin(session);
    validatePaging(page, size);
    return baoCaoService.inventorySummary(categoryId, zeroStock, lowStock, page, size);
  }

  @GetMapping("/products/low-stock")
  public PagedResponse<InventoryProductResponse> lowStock(
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      HttpSession session) {
    requireAdmin(session);
    validatePaging(page, size);
    return baoCaoService.lowStock(categoryId, page, size);
  }

  @GetMapping("/products/slow-moving")
  public PagedResponse<InventoryProductResponse> slowMoving(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer quarter,
      @RequestParam(required = false) Integer categoryId,
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      HttpSession session) {
    requireAdmin(session);
    DateRange range = resolveRange(startDate, endDate, year, month, quarter);
    validatePaging(page, size);
    return baoCaoService.slowMoving(range.start(), range.end(), categoryId, search, page, size);
  }

  @GetMapping("/inventory/movements")
  public PagedResponse<StockMovementResponse> stockMovements(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(required = false) Integer variantId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      HttpSession session) {
    requireAdmin(session);
    validatePaging(page, size);
    if ((startDate == null) != (endDate == null)) {
      throw new IllegalArgumentException("Cần truyền đồng thời startDate và endDate.");
    }
    return baoCaoService.stockMovements(startDate, endDate, variantId, page, size);
  }

  private void requireAdmin(HttpSession session) {
    Object value = session.getAttribute("taikhoan");
    if (!(value instanceof TaiKhoanLogin account)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập.");
    }
    if (account.getMachucvu() != 1) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ quản trị viên được truy cập.");
    }
  }

  private DateRange resolveRange(
      LocalDate startDate, LocalDate endDate, Integer year, Integer month, Integer quarter) {
    int specified = (startDate == null ? 0 : 1) + (endDate == null ? 0 : 1);
    if (specified == 1) {
      throw new IllegalArgumentException("Cần truyền đồng thời startDate và endDate.");
    }
    if (specified == 2) {
      if (year != null || month != null || quarter != null) {
        throw new IllegalArgumentException("Không kết hợp ngày tùy chọn với year/month/quarter.");
      }
      return new DateRange(startDate, endDate);
    }
    if (year == null && month == null && quarter == null) {
      LocalDate now = LocalDate.now();
      return new DateRange(now.withDayOfMonth(1), now.withDayOfMonth(now.lengthOfMonth()));
    }
    if (year == null || (month != null && quarter != null)) {
      throw new IllegalArgumentException("Tham số year/month/quarter không hợp lệ.");
    }
    if (month != null) {
      LocalDate first = LocalDate.of(year, month, 1);
      return new DateRange(first, first.withDayOfMonth(first.lengthOfMonth()));
    }
    if (quarter != null) {
      if (quarter < 1 || quarter > 4) {
        throw new IllegalArgumentException("quarter phải từ 1 đến 4.");
      }
      LocalDate first = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
      return new DateRange(first, first.plusMonths(3).minusDays(1));
    }
    LocalDate first = LocalDate.of(year, 1, 1);
    return new DateRange(first, LocalDate.of(year, 12, 31));
  }

  private void validatePaging(int page, int size) {
    if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
      throw new IllegalArgumentException("page phải >= 0 và size phải từ 1 đến 100.");
    }
  }

  private record DateRange(LocalDate start, LocalDate end) {
    private DateRange {
      if (start == null || end == null || end.isBefore(start)) {
        throw new IllegalArgumentException("Khoảng ngày không hợp lệ.");
      }
    }

    private long days() {
      return java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
    }
  }
}

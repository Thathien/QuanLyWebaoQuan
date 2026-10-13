package com.banhang.dao;

import com.banhang.entity.TrangThaiHoaDon;
import com.banhang.entity.TrangThaiPhieuNhap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class BaoCaoDao {
  @PersistenceContext private EntityManager entityManager;

  static final String BEST_SELLING_PROJECTION =
      """
      SELECT p.masanpham, CAST(p.masanpham AS nvarchar(40)), p.tensanpham,
             c.madanhmucsanpham, c.tendanhmuc,
             COALESCE(s.quantity_sold, 0) AS quantity_sold,
             COALESCE(s.gross_revenue, 0) AS gross_revenue,
             COALESCE(r.refunded, 0) AS refunded,
             COALESCE(s.gross_revenue, 0) - COALESCE(r.refunded, 0) AS net_revenue,
             CASE WHEN COALESCE(s.quantity_sold, 0) = 0 THEN 0
                  ELSE s.gross_revenue / s.quantity_sold END AS average_selling_price,
             CASE WHEN s.missing_cost > 0 OR rc.missing_return_cost > 0 THEN NULL
                  ELSE COALESCE(s.cogs, 0) - COALESCE(rc.returned_cost, 0) END
                  AS cost_of_goods_sold
      """;

  public Object[] revenueSummary(LocalDateTime start, LocalDateTime end, int completedStatus) {
    Object[] sales =
        (Object[])
            entityManager
                .createNativeQuery(
                    """
                SELECT
                  COALESCE(SUM(TRY_CONVERT(decimal(19,2), d.giatien) * d.soluong), 0),
                  COUNT(DISTINCT h.mahoadon),
                  COALESCE(SUM(d.soluong), 0),
                  SUM(d.gia_von * d.soluong),
                  SUM(CASE WHEN d.gia_von IS NULL THEN 1 ELSE 0 END)
                FROM hoa_don h
                JOIN chi_tiet_hoa_don d ON d.mahoadon = h.mahoadon
                WHERE h.tinhtrang = :completedStatus
                  AND h.completed_at >= :start AND h.completed_at < :end
                """)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("completedStatus", completedStatus)
                .getSingleResult();
    Object[] returnedGoods =
        (Object[])
            entityManager
                .createNativeQuery(
                    """
                SELECT COALESCE(SUM(rt.unit_cost * rt.quantity), 0),
                       SUM(CASE WHEN rt.unit_cost IS NULL THEN 1 ELSE 0 END)
                FROM tra_hang tr
                JOIN chi_tiet_tra_hang rt ON rt.return_id = tr.id
                WHERE tr.received_at >= :start AND tr.received_at < :end
                """)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    Object[] refundAmounts =
        (Object[])
            entityManager
                .createNativeQuery(
                    """
                SELECT COALESCE(SUM(rf.amount), 0), COALESCE(SUM(rf.quantity), 0)
                FROM hoan_tien r
                JOIN chi_tiet_hoan_tien rf ON rf.refund_id = r.id
                WHERE r.refunded_at >= :start AND r.refunded_at < :end
                """)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    return new Object[] {
      sales[0],
      sales[1],
      sales[2],
      sales[3],
      sales[4],
      refundAmounts[0],
      refundAmounts[1],
      returnedGoods[0],
      returnedGoods[1]
    };
  }

  public List<Object[]> revenueSeries(
      LocalDateTime start, LocalDateTime end, String granularity, int completedStatus) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows =
        entityManager
            .createNativeQuery(revenueSeriesSql(granularity))
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("completedStatus", completedStatus)
            .getResultList();
    return rows;
  }

  static String revenueSeriesSql(String granularity) {
    String salesBucket = bucketExpression(granularity, "h.completed_at");
    String refundBucket = bucketExpression(granularity, "r.refunded_at");
    return """
        SELECT period_start, SUM(gross_revenue), SUM(refunds), SUM(order_count),
               SUM(quantity_sold), SUM(cost_of_goods_sold)
        FROM (
          SELECT %1$s AS period_start,
                 SUM(TRY_CONVERT(decimal(19,2), d.giatien) * d.soluong) AS gross_revenue,
                 CAST(0 AS decimal(19,2)) AS refunds,
                 COUNT(DISTINCT h.mahoadon) AS order_count,
                 SUM(d.soluong) AS quantity_sold,
                 SUM(d.gia_von * d.soluong) AS cost_of_goods_sold
          FROM hoa_don h
          JOIN chi_tiet_hoa_don d ON d.mahoadon = h.mahoadon
          WHERE h.tinhtrang = :completedStatus
            AND h.completed_at >= :start AND h.completed_at < :end
          GROUP BY %1$s
          UNION ALL
          SELECT %2$s AS period_start, 0, SUM(rf.amount), 0, 0, 0
          FROM hoan_tien r
          JOIN chi_tiet_hoan_tien rf ON rf.refund_id = r.id
          WHERE r.refunded_at >= :start AND r.refunded_at < :end
          GROUP BY %2$s
        ) totals
        GROUP BY period_start
        ORDER BY period_start
        """
        .formatted(salesBucket, refundBucket);
  }

  public List<Object[]> bestSelling(
      LocalDateTime start,
      LocalDateTime end,
      Integer categoryId,
      String search,
      String sortBy,
      int completedStatus,
      int offset,
      int limit) {
    Query query =
        entityManager.createNativeQuery(
            """
                WITH sales AS (
                  SELECT p.masanpham, SUM(d.soluong) quantity_sold,
                         SUM(TRY_CONVERT(decimal(19,2), d.giatien) * d.soluong) gross_revenue,
                         SUM(d.gia_von * d.soluong) cogs,
                         SUM(CASE WHEN d.gia_von IS NULL THEN 1 ELSE 0 END) missing_cost
                  FROM hoa_don h
                  JOIN chi_tiet_hoa_don d ON d.mahoadon = h.mahoadon
                  JOIN chi_tiet_san_pham v ON v.machitietsanpham = d.machitietsanpham
                  JOIN san_pham p ON p.masanpham = v.masanpham
                  WHERE h.tinhtrang = :completedStatus
                    AND h.completed_at >= :start AND h.completed_at < :end
                  GROUP BY p.masanpham
                ), refunds AS (
                  SELECT p.masanpham, SUM(rf.amount) refunded
                  FROM hoan_tien r
                  JOIN chi_tiet_hoan_tien rf ON rf.refund_id = r.id
                  JOIN chi_tiet_san_pham v ON v.machitietsanpham = rf.variant_id
                  JOIN san_pham p ON p.masanpham = v.masanpham
                  WHERE r.refunded_at >= :start AND r.refunded_at < :end
                  GROUP BY p.masanpham
                ), returned_costs AS (
                  SELECT p.masanpham, SUM(rt.unit_cost * rt.quantity) returned_cost,
                         SUM(CASE WHEN rt.unit_cost IS NULL THEN 1 ELSE 0 END) missing_return_cost
                  FROM tra_hang tr
                  JOIN chi_tiet_tra_hang rt ON rt.return_id = tr.id
                  JOIN chi_tiet_san_pham v ON v.machitietsanpham = rt.variant_id
                  JOIN san_pham p ON p.masanpham = v.masanpham
                  WHERE tr.received_at >= :start AND tr.received_at < :end
                  GROUP BY p.masanpham
                )
                """
                + BEST_SELLING_PROJECTION
                + """
                FROM san_pham p
                LEFT JOIN danh_muc_san_pham c ON c.madanhmucsanpham = p.madanhmucsanpham
                LEFT JOIN sales s ON s.masanpham = p.masanpham
                LEFT JOIN refunds r ON r.masanpham = p.masanpham
                LEFT JOIN returned_costs rc ON rc.masanpham = p.masanpham
                WHERE (:categoryId < 0 OR c.madanhmucsanpham = :categoryId)
                  AND (:search = '' OR p.tensanpham LIKE :search
                       OR CAST(p.masanpham AS nvarchar(40)) LIKE :search)
                  AND COALESCE(s.quantity_sold, 0) > 0
                """
                + bestSellingOrderBy(sortBy));
    @SuppressWarnings("unchecked")
    List<Object[]> rows =
        query
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("completedStatus", completedStatus)
            .setParameter("categoryId", categoryId == null ? -1 : categoryId)
            .setParameter(
                "search", search == null || search.isBlank() ? "" : "%" + search.trim() + "%")
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    return rows;
  }

  static String bestSellingOrderBy(String sortBy) {
    String orderColumn = "quantity".equals(sortBy) ? "quantity_sold" : "net_revenue";
    return "ORDER BY " + orderColumn + " DESC, p.masanpham ASC";
  }

  public List<Object[]> mostImported(
      LocalDateTime start,
      LocalDateTime end,
      Integer categoryId,
      Long supplierId,
      String search,
      int offset,
      int limit) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows =
        entityManager
            .createNativeQuery(
                """
                SELECT p.masanpham, p.tensanpham, c.madanhmucsanpham, c.tendanhmuc,
                       SUM(d.quantity), SUM(d.quantity * d.unit_cost),
                       COUNT(DISTINCT r.id), AVG(d.unit_cost), MAX(r.confirmed_at)
                FROM phieu_nhap r
                JOIN chi_tiet_phieu_nhap d ON d.receipt_id = r.id
                JOIN chi_tiet_san_pham v ON v.machitietsanpham = d.variant_id
                JOIN san_pham p ON p.masanpham = v.masanpham
                LEFT JOIN danh_muc_san_pham c ON c.madanhmucsanpham = p.madanhmucsanpham
                WHERE r.status = :confirmedStatus
                  AND r.confirmed_at >= :start AND r.confirmed_at < :end
                  AND (:categoryId < 0 OR c.madanhmucsanpham = :categoryId)
                  AND (:supplierId < 0 OR r.supplier_id = :supplierId)
                  AND (:search = '' OR p.tensanpham LIKE :search
                       OR CAST(p.masanpham AS nvarchar(40)) LIKE :search)
                GROUP BY p.masanpham, p.tensanpham, c.madanhmucsanpham, c.tendanhmuc
                ORDER BY SUM(d.quantity) DESC, p.masanpham ASC
                """)
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("confirmedStatus", TrangThaiPhieuNhap.DA_XAC_NHAN.getCode())
            .setParameter("categoryId", categoryId == null ? -1 : categoryId)
            .setParameter("supplierId", supplierId == null ? -1L : supplierId)
            .setParameter(
                "search", search == null || search.isBlank() ? "" : "%" + search.trim() + "%")
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    return rows;
  }

  public Object[] inventorySummary() {
    return (Object[])
        entityManager
            .createNativeQuery(
                """
                SELECT COUNT(*), COALESCE(SUM(v.soluong), 0),
                       SUM(CASE WHEN v.soluong = 0 THEN 1 ELSE 0 END),
                       SUM(CASE WHEN v.soluong <= v.ton_toi_thieu THEN 1 ELSE 0 END),
                       SUM(CASE WHEN v.soluong > 0 AND v.gia_von_binh_quan IS NULL THEN 1 ELSE 0 END),
                       SUM(v.soluong * v.gia_von_binh_quan)
                FROM chi_tiet_san_pham v
                """)
            .getSingleResult();
  }

  public List<Object[]> inventoryProducts(
      Integer categoryId, Boolean zeroStock, Boolean lowStock, int offset, int limit) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows =
        entityManager
            .createNativeQuery(
                """
                SELECT p.masanpham, p.tensanpham, c.madanhmucsanpham, c.tendanhmuc,
                       SUM(v.soluong), SUM(v.ton_toi_thieu),
                       CASE WHEN SUM(CASE WHEN v.soluong > 0 AND v.gia_von_binh_quan IS NULL
                                          THEN 1 ELSE 0 END) > 0
                            THEN NULL ELSE SUM(v.soluong * v.gia_von_binh_quan) END,
                       SUM(CASE WHEN v.soluong > 0 AND v.gia_von_binh_quan IS NULL
                                THEN 1 ELSE 0 END)
                FROM san_pham p
                JOIN chi_tiet_san_pham v ON v.masanpham = p.masanpham
                LEFT JOIN danh_muc_san_pham c ON c.madanhmucsanpham = p.madanhmucsanpham
                WHERE (:categoryId < 0 OR c.madanhmucsanpham = :categoryId)
                GROUP BY p.masanpham, p.tensanpham, c.madanhmucsanpham, c.tendanhmuc
                HAVING (:zeroStockMode < 0 OR
                        (:zeroStockMode = 1 AND SUM(v.soluong) = 0) OR
                        (:zeroStockMode = 0 AND SUM(v.soluong) > 0))
                   AND (:lowStockMode < 0 OR
                        (:lowStockMode = 1 AND SUM(v.soluong) <= SUM(v.ton_toi_thieu)) OR
                        (:lowStockMode = 0 AND SUM(v.soluong) > SUM(v.ton_toi_thieu)))
                ORDER BY SUM(v.soluong) ASC, p.masanpham
                """)
            .setParameter("categoryId", categoryId == null ? -1 : categoryId)
            .setParameter("zeroStockMode", zeroStock == null ? -1 : zeroStock ? 1 : 0)
            .setParameter("lowStockMode", lowStock == null ? -1 : lowStock ? 1 : 0)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    return rows;
  }

  public List<Object[]> slowMoving(
      LocalDateTime start,
      LocalDateTime end,
      Integer categoryId,
      String search,
      int offset,
      int limit) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows =
        entityManager
            .createNativeQuery(
                """
                SELECT p.masanpham, p.tensanpham, c.madanhmucsanpham, c.tendanhmuc,
                       COALESCE(SUM(v.soluong), 0),
                       CASE WHEN COUNT(v.machitietsanpham) = 0 THEN 0
                            ELSE SUM(CASE WHEN v.gia_von_binh_quan IS NULL THEN 1 ELSE 0 END) END
                FROM san_pham p
                LEFT JOIN danh_muc_san_pham c ON c.madanhmucsanpham = p.madanhmucsanpham
                LEFT JOIN chi_tiet_san_pham v ON v.masanpham = p.masanpham
                WHERE (:categoryId < 0 OR c.madanhmucsanpham = :categoryId)
                  AND (:search = '' OR p.tensanpham LIKE :search
                       OR CAST(p.masanpham AS nvarchar(40)) LIKE :search)
                  AND NOT EXISTS (
                    SELECT 1
                    FROM hoa_don h
                    JOIN chi_tiet_hoa_don d ON d.mahoadon = h.mahoadon
                    JOIN chi_tiet_san_pham sold ON sold.machitietsanpham = d.machitietsanpham
                    WHERE sold.masanpham = p.masanpham
                      AND h.tinhtrang = :completedStatus
                      AND h.completed_at >= :start AND h.completed_at < :end
                  )
                GROUP BY p.masanpham, p.tensanpham, c.madanhmucsanpham, c.tendanhmuc
                ORDER BY p.masanpham
                """)
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("completedStatus", TrangThaiHoaDon.HOAN_THANH.getCode())
            .setParameter("categoryId", categoryId == null ? -1 : categoryId)
            .setParameter(
                "search", search == null || search.isBlank() ? "" : "%" + search.trim() + "%")
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    return rows;
  }

  public List<Object[]> stockMovements(
      LocalDateTime start, LocalDateTime end, Integer variantId, int offset, int limit) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows =
        entityManager
            .createNativeQuery(
                """
                SELECT m.id, m.occurred_at, v.machitietsanpham, p.masanpham, p.tensanpham,
                       m.movement_type, m.quantity_delta, m.unit_cost, m.reference_id, m.note
                FROM bien_dong_kho m
                JOIN chi_tiet_san_pham v ON v.machitietsanpham = m.variant_id
                JOIN san_pham p ON p.masanpham = v.masanpham
                WHERE m.occurred_at >= :start AND m.occurred_at < :end
                  AND (:variantId < 0 OR m.variant_id = :variantId)
                ORDER BY m.occurred_at DESC, m.id DESC
                """)
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("variantId", variantId == null ? -1 : variantId)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    return rows;
  }

  private static String bucketExpression(String granularity, String column) {
    return switch (granularity) {
      case "day" -> "CAST(" + column + " AS date)";
      case "month" -> "DATEFROMPARTS(YEAR(" + column + "), MONTH(" + column + "), 1)";
      case "quarter" ->
          "DATEFROMPARTS(YEAR("
              + column
              + "), ((DATEPART(QUARTER, "
              + column
              + ") - 1) * 3) + 1, 1)";
      case "year" -> "DATEFROMPARTS(YEAR(" + column + "), 1, 1)";
      default -> throw new IllegalArgumentException("Khoảng thống kê không hợp lệ.");
    };
  }
}

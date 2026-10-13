package com.banhang.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BaoCaoDaoTest {
  @Test
  void bestSellingOrderClauseSeparatesSqlKeywordsAndColumn() {
    assertEquals(
        "ORDER BY net_revenue DESC, p.masanpham ASC", BaoCaoDao.bestSellingOrderBy("revenue"));
    assertEquals(
        "ORDER BY quantity_sold DESC, p.masanpham ASC", BaoCaoDao.bestSellingOrderBy("quantity"));
  }

  @Test
  void bestSellingProjectionAliasesColumnsUsedForSorting() {
    assertTrue(BaoCaoDao.BEST_SELLING_PROJECTION.contains("AS quantity_sold"));
    assertTrue(BaoCaoDao.BEST_SELLING_PROJECTION.contains("AS net_revenue"));
  }

  @Test
  void revenueSeriesSqlSeparatesBucketExpressionsFromSqlKeywords() {
    String sql = BaoCaoDao.revenueSeriesSql("day");

    assertTrue(sql.contains("SELECT CAST(h.completed_at AS date) AS period_start"));
    assertTrue(sql.contains("GROUP BY CAST(h.completed_at AS date)\n  UNION ALL"));
    assertTrue(sql.contains("SELECT CAST(r.refunded_at AS date) AS period_start"));
    assertTrue(sql.contains("GROUP BY CAST(r.refunded_at AS date)\n) totals"));
  }
}

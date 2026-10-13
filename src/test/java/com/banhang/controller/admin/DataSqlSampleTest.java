package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class DataSqlSampleTest {
  @Test
  void sampleOrdersAndInventoryTransactionsAreSeededAfterCleanup() throws IOException {
    String sql = Files.readString(Path.of("src/main/resources/data.sql"));
    int orderInsertStart = sql.indexOf("INSERT INTO [hoa_don]");
    int orderInsertEnd = sql.indexOf("SET IDENTITY_INSERT [hoa_don] OFF", orderInsertStart);
    String orderRows = sql.substring(orderInsertStart, orderInsertEnd);

    assertTrue(Pattern.compile("(?m)^\\([1-8], N'").matcher(orderRows).results().count() >= 8);
    assertTrue(sql.contains("INSERT INTO [chi_tiet_hoa_don]"));
    assertTrue(sql.contains("INSERT INTO [chi_tiet_hoan_tien]"));
    assertTrue(sql.contains("INSERT INTO [chi_tiet_tra_hang]"));
    assertTrue(sql.contains("N'CUSTOMER_RETURN'"));
    assertTrue(
        sql.indexOf("DELETE FROM [chi_tiet_hoa_don]") < sql.indexOf("DELETE FROM [hoa_don]"));
  }

  @Test
  void hoaDonStatusIndexIsDroppedBeforeTypeMigrationAndRecreated() throws IOException {
    String sql = Files.readString(Path.of("src/main/resources/data.sql"));

    assertTrue(
        sql.indexOf("DROP INDEX [IX_HoaDon_Status_CompletedAt]")
            < sql.indexOf("ALTER COLUMN [tinhtrang] INT"));
    assertTrue(sql.contains("CREATE INDEX [IX_HoaDon_Status_CompletedAt]"));
  }
}

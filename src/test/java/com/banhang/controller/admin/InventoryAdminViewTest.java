package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class InventoryAdminViewTest {
  @Test
  void inventoryViewExposesReceiptSupplierAndStockActions() throws IOException {
    String view =
        Files.readString(Path.of("src/main/webapp/WEB-INF/view/admin/inventory_admin.jsp"));

    assertTrue(view.contains("id=\"receipt-form\""));
    assertTrue(view.contains("id=\"supplier-form\""));
    assertTrue(view.contains("id=\"adjustment-form\""));
    assertTrue(view.contains("id=\"opening-form\""));
    assertTrue(view.contains("/api/inventory/receipts"));
    assertTrue(view.contains("/api/inventory/variants"));
    assertTrue(view.contains("/api/inventory/adjustments"));
    assertTrue(view.contains("/api/inventory/opening-balances"));
  }
}

package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ProductsAdminViewTest {
  @Test
  void productsViewIncludesModalPaginationBulkDeleteImagesAndPriceFormatting() throws IOException {
    String view =
        Files.readString(Path.of("src/main/webapp/WEB-INF/view/admin/products_admin.jsp"));

    assertTrue(view.contains("<dialog id=\"product-modal\""));
    assertTrue(view.contains("id=\"product-page-size\""));
    assertTrue(view.contains("id=\"delete-selected\""));
    assertTrue(view.contains("product-thumbnail"));
    assertTrue(view.contains("product12.jpg"));
    assertTrue(view.contains("replace(/\\B(?=(\\d{3})+(?!\\d))/g, ',')"));
  }
}

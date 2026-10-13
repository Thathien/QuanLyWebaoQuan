package com.banhang.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ProductImageSupportTest {
  @Test
  void returnsDefaultForMissingOrUnsupportedImage() {
    assertEquals("product12.jpg", ProductImageSupport.safeFileName(null));
    assertEquals("product12.jpg", ProductImageSupport.safeFileName(" "));
    assertEquals("product12.jpg", ProductImageSupport.safeFileName("image.svg"));
  }

  @Test
  void storesAndResolvesOnlySafeImageFileName() {
    assertEquals(
        "photo.webp",
        ProductImageSupport.safeFileName("resourcess\\user\\images\\shop\\photo.webp"));
    assertEquals("product12.jpg", ProductImageSupport.safeFileName("../photo.exe"));
  }

  @Test
  void seededProductImagesExistInTheShopImageDirectory() {
    Path shopImages = Path.of("src/main/webapp/resourcess/user/images/shop");

    for (int imageNumber = 1; imageNumber <= 12; imageNumber++) {
      assertTrue(
          Files.isRegularFile(shopImages.resolve("product" + imageNumber + ".jpg")),
          "Missing seeded product image: product" + imageNumber + ".jpg");
    }
  }
}

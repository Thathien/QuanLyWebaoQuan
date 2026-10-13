package com.banhang.commons;

import java.util.List;
import java.util.Locale;

public final class ProductImageSupport {
  public static final String DEFAULT_IMAGE = "product12.jpg";

  private static final List<String> ALLOWED_EXTENSIONS =
      List.of("jpg", "jpeg", "png", "gif", "webp");

  private ProductImageSupport() {}

  public static String safeFileName(String image) {
    if (image == null || image.isBlank()) {
      return DEFAULT_IMAGE;
    }
    String normalized = image.trim().replace('\\', '/');
    String fileName = normalized.substring(normalized.lastIndexOf('/') + 1);
    int extensionIndex = fileName.lastIndexOf('.');
    if (extensionIndex <= 0 || extensionIndex == fileName.length() - 1) {
      return DEFAULT_IMAGE;
    }
    String extension = fileName.substring(extensionIndex + 1).toLowerCase(Locale.ROOT);
    return ALLOWED_EXTENSIONS.contains(extension) ? fileName : DEFAULT_IMAGE;
  }
}

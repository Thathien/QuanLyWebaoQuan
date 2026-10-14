package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class AdminPageEncodingTest {
  @Test
  void adminJspPagesAndServerResponsesUseUtf8() throws IOException {
    Path viewDirectory = Path.of("src/main/webapp/WEB-INF/view/admin");
    try (Stream<Path> pages = Files.list(viewDirectory)) {
      pages
          .filter(path -> path.getFileName().toString().endsWith("admin.jsp"))
          .filter(path -> !path.getFileName().toString().equals("Footer_admin.jsp"))
          .forEach(
              path -> {
                try {
                  String page = Files.readString(path);
                  assertTrue(
                      page.contains("contentType=\"text/html; charset=UTF-8\""),
                      path.getFileName() + " must declare a UTF-8 response.");
                  assertTrue(
                      page.contains("pageEncoding=\"UTF-8\""),
                      path.getFileName() + " must be read as UTF-8.");
                } catch (IOException e) {
                  throw new IllegalStateException("Unable to read admin JSP " + path, e);
                }
              });
    }

    String properties = Files.readString(Path.of("src/main/resources/application.properties"));
    assertTrue(properties.contains("server.servlet.encoding.charset=UTF-8"));
    assertTrue(properties.contains("server.servlet.encoding.force-request=true"));
    assertTrue(properties.contains("server.servlet.encoding.force-response=true"));
  }
}

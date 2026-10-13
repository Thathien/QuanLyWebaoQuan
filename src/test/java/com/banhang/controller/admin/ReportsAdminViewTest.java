package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ReportsAdminViewTest {
  @Test
  void inlineJavaScriptDoesNotUseJspElInterpolationSyntax() throws IOException {
    String view = Files.readString(Path.of("src/main/webapp/WEB-INF/view/reports_admin.jsp"));
    int scriptStart = view.indexOf("<script>");
    int scriptEnd = view.indexOf("</script>", scriptStart);

    assertFalse(scriptStart < 0 || scriptEnd < 0, "Reports page must contain its inline script.");
    assertFalse(
        view.substring(scriptStart, scriptEnd).contains("${"),
        "JavaScript interpolation is parsed as JSP EL inside JSP views.");
  }
}

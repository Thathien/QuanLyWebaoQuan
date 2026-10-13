package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.dto.report.InventoryVariantResponse;
import com.banhang.dto.report.ReceiptAdminResponse;
import com.banhang.service.KhoService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.server.ResponseStatusException;

class KhoAdminControllerTest {
  private final KhoService khoService = org.mockito.Mockito.mock(KhoService.class);
  private final KhoAdminController controller = new KhoAdminController(khoService);

  @Test
  void inventoryEndpointsRequireAdminAndReturnReceiptAndVariantData() {
    InventoryVariantResponse variant =
        new InventoryVariantResponse(2, "Áo thun", "M", "Đen", 5, null, false);
    ReceiptAdminResponse receipt =
        new ReceiptAdminResponse(3L, 1L, "Nhà cung cấp", "DRAFT", null, null, List.of());
    when(khoService.getVariants()).thenReturn(List.of(variant));
    when(khoService.getReceipts()).thenReturn(List.of(receipt));

    assertThrows(ResponseStatusException.class, () -> controller.variants(new MockHttpSession()));
    assertEquals(List.of(variant), controller.variants(sessionWithRole(1)));
    assertEquals(List.of(receipt), controller.receipts(sessionWithRole(1)));
    verify(khoService).getVariants();
    verify(khoService).getReceipts();
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }
}

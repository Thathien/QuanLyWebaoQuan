package com.banhang.service;

import static org.junit.jupiter.api.Assertions.*;

import com.banhang.entity.GioHang;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

class GioHangServiceTest {
  private final GioHangService service = new GioHangService();

  @Test
  void addItemCreatesCartAndMergesSameProductVariant() {
    MockHttpSession session = new MockHttpSession();

    service.addItem(10, 1, 2, 3, "Shirt", "120", "Blue", "M", 2, session);
    service.addItem(10, 1, 2, 3, "Shirt", "120", "Blue", "M", 3, session);

    List<GioHang> cart = service.getCart(session);
    assertEquals(1, cart.size());
    assertEquals(5, cart.get(0).getSoluong());
    assertEquals(10, cart.get(0).getMachitietsanpham());
    assertEquals("120", cart.get(0).getGiatien());
  }

  @Test
  void differentVariantsRemainSeparateAndCanBeUpdatedOrRemoved() {
    MockHttpSession session = new MockHttpSession();
    service.addItem(10, 1, 2, 3, "Shirt", "120", "Blue", "M", 1, session);
    service.addItem(11, 1, 3, 3, "Shirt", "120", "Blue", "L", 1, session);

    service.updateQuantity(1, 2, 3, 4, session);
    assertEquals(4, service.getCart(session).get(0).getSoluong());

    service.removeItem(1, 2, 3, session);
    assertEquals(1, service.getCart(session).size());
    assertEquals(11, service.getCart(session).get(0).getMachitietsanpham());
  }

  @Test
  void getCartInitializesSessionAndHasCartReflectsPresence() {
    MockHttpSession session = new MockHttpSession();

    assertFalse(service.hasCart(session));
    assertTrue(service.getCart(session).isEmpty());
    assertTrue(service.hasCart(session));
  }
}

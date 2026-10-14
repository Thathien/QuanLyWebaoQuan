package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.HoaDon;
import com.banhang.service.HoaDonService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;

class QuanLyHoaDonAdminControllerTest {
  private final HoaDonService hoaDonService = mock(HoaDonService.class);
  private final QuanLyHoaDonAdminController controller =
      new QuanLyHoaDonAdminController(hoaDonService);

  @Test
  void orderListIsRestrictedToAdmin() {
    assertEquals(
        "redirect:/admin/dangnhap", controller.listOrders(new ModelMap(), new MockHttpSession()));
    assertEquals("401", controller.listOrders(new ModelMap(), sessionWithRole(3)));
    verifyNoInteractions(hoaDonService);

    assertEquals("admin/MG_order_admin", controller.listOrders(new ModelMap(), sessionWithRole(1)));
    verify(hoaDonService).getAllHoaDon();
  }

  @Test
  void orderStatusMustMoveToNextSupportedState() {
    MockHttpSession adminSession = sessionWithRole(1);
    ModelMap invalidModel = new ModelMap();
    assertEquals("admin/MG_order_admin", controller.updateStatus(7, 2, invalidModel, adminSession));
    assertEquals(
        "Không tìm thấy đơn hàng hoặc trạng thái không hợp lệ.", invalidModel.get("orderError"));
    verify(hoaDonService).getAllHoaDon();
    verify(hoaDonService, never()).updateHoaDon(any());

    HoaDon order = new HoaDon();
    order.setTinhtrang(1);
    when(hoaDonService.getHoaDonById(7)).thenReturn(order);
    when(hoaDonService.updateHoaDon(order)).thenReturn(true);
    ModelMap validModel = new ModelMap();
    assertEquals("admin/MG_order_admin", controller.updateStatus(7, 2, validModel, adminSession));
    assertEquals(2, order.getTinhtrang());
    assertEquals("Đã cập nhật trạng thái đơn hàng.", validModel.get("orderResult"));
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }
}

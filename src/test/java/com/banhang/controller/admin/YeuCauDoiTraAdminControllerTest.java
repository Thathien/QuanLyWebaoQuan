package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.TrangThaiYeuCauDoiTra;
import com.banhang.service.YeuCauDoiTraService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;

@ExtendWith(MockitoExtension.class)
class YeuCauDoiTraAdminControllerTest {
  @Mock private YeuCauDoiTraService yeuCauDoiTraService;

  private YeuCauDoiTraAdminController controller;

  @BeforeEach
  void setUp() {
    controller = new YeuCauDoiTraAdminController(yeuCauDoiTraService);
  }

  @Test
  void returnRequestQueueRequiresAdmin() {
    assertEquals(
        "redirect:/admin/dangnhap", controller.listRequests(new MockHttpSession(), new ModelMap()));

    MockHttpSession customerSession = new MockHttpSession();
    customerSession.setAttribute("taikhoan", new TaiKhoanLogin(2, "customer", "", "Customer", 3));
    assertEquals("401", controller.listRequests(customerSession, new ModelMap()));

    MockHttpSession adminSession = adminSession();
    when(yeuCauDoiTraService.getAllRequests()).thenReturn(List.of());
    assertEquals(
        "admin/return-requests_admin", controller.listRequests(adminSession, new ModelMap()));
  }

  @Test
  void adminCanAcceptARequest() {
    ModelMap model = new ModelMap();
    when(yeuCauDoiTraService.getAllRequests()).thenReturn(List.of());

    assertEquals(
        "admin/return-requests_admin",
        controller.updateRequestStatus(14, "CHAP_NHAN", adminSession(), model));

    verify(yeuCauDoiTraService).updateRequestStatus(14, TrangThaiYeuCauDoiTra.CHAP_NHAN);
    verify(yeuCauDoiTraService).getAllRequests();
  }

  private MockHttpSession adminSession() {
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("taikhoan", new TaiKhoanLogin(1, "admin", "", "Admin", 1));
    return session;
  }
}

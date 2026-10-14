package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.service.HoaDonService;
import com.banhang.service.SanPhamService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;

class TrangChuAdminControllerTest {
  private final SanPhamService sanPhamService = Mockito.mock(SanPhamService.class);
  private final HoaDonService hoaDonService = Mockito.mock(HoaDonService.class);
  private final TrangChuAdminController controller =
      new TrangChuAdminController(sanPhamService, hoaDonService);

  @BeforeEach
  void setUp() {
    when(sanPhamService.getAllSanPham()).thenReturn(List.of());
    when(hoaDonService.getAllHoaDon()).thenReturn(List.of());
  }

  @Test
  void dashboardAllowsOnlyAdminRole() {
    assertEquals(
        "redirect:/admin/dangnhap", controller.index(new ModelMap(), new MockHttpSession()));

    MockHttpSession customerSession = sessionWithRole(3);
    assertEquals("401", controller.index(new ModelMap(), customerSession));

    assertEquals("admin/index_admin", controller.index(new ModelMap(), sessionWithRole(1)));
  }

  @Test
  void reportsPageRequiresAdminRole() {
    assertEquals("redirect:/admin/dangnhap", controller.reports(new MockHttpSession()));
    assertEquals("401", controller.reports(sessionWithRole(3)));
    assertEquals("admin/reports_admin", controller.reports(sessionWithRole(1)));
  }

  @Test
  void reportsPageIsMappedAtAdminReports() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    mockMvc
        .perform(get("/admin/reports").session(sessionWithRole(1)))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/reports_admin"));
    mockMvc
        .perform(get("/admin/reports").session(sessionWithRole(3)))
        .andExpect(status().isOk())
        .andExpect(view().name("401"));
    mockMvc
        .perform(get("/admin/admin/reports").session(sessionWithRole(1)))
        .andExpect(status().isNotFound());
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }
}

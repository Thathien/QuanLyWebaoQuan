package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.banhang.commons.TaiKhoanLogin;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class KhoAdminPageControllerTest {
  private final KhoAdminPageController controller = new KhoAdminPageController();

  @Test
  void inventoryPageRequiresAdminRole() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    mockMvc
        .perform(get("/admin/inventory"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/admin/dangnhap"));
    mockMvc
        .perform(get("/admin/inventory").session(sessionWithRole(3)))
        .andExpect(status().isOk())
        .andExpect(view().name("401"));
    mockMvc
        .perform(get("/admin/inventory").session(sessionWithRole(1)))
        .andExpect(status().isOk())
        .andExpect(view().name("inventory_admin"));
  }

  @Test
  void inventoryPageUsesExpectedRoute() {
    assertEquals("inventory_admin", controller.inventory(sessionWithRole(1)));
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }
}

package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChucVu;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangNhapModel;
import com.banhang.service.NhanVienService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

@ExtendWith(MockitoExtension.class)
class TaiKhoanAdminControllerTest {
  @Mock private NhanVienService nhanVienService;

  private TaiKhoanAdminController controller;

  @BeforeEach
  void setUp() {
    controller = new TaiKhoanAdminController(nhanVienService);
  }

  @Test
  void loginPageRedirectsExistingRolesOrShowsForm() {
    ExtendedModelMap model = new ExtendedModelMap();
    assertEquals("admin/login_admin", controller.showDangNhap(model, new MockHttpSession()));
    assertTrue(model.containsAttribute("dangnhapadmin"));
    assertEquals("redirect:/admin", controller.showDangNhap(model, sessionWithRole(1)));
    assertEquals("redirect:/", controller.showDangNhap(model, sessionWithRole(3)));
  }

  @Test
  void loginRejectsInvalidCredentialsAndCreatesAdminSession() {
    DangNhapModel credentials = new DangNhapModel("admin@example.com", "secret");
    BeanPropertyBindingResult binding = new BeanPropertyBindingResult(credentials, "dangnhapadmin");
    ModelMap model = new ModelMap();
    when(nhanVienService.getAdminByCredentials("admin@example.com", "secret")).thenReturn(null);

    assertEquals("admin/login_admin", controller.dangNhapProcess(credentials, binding, model));
    assertTrue(model.containsAttribute("resultsAdmin"));

    NhanVien admin = new NhanVien();
    admin.setManhanvien(1);
    admin.setHoten("Administrator");
    admin.setEmail("admin@example.com");
    admin.setTendangnhap("admin");
    admin.setMatkhau("secret");
    admin.setChucVu(ChucVu.builder().machucvu(1).build());
    when(nhanVienService.getAdminByCredentials("admin@example.com", "secret")).thenReturn(admin);

    assertEquals("redirect:/admin", controller.dangNhapProcess(credentials, binding, model));
    TaiKhoanLogin account = (TaiKhoanLogin) model.get("taikhoan");
    assertEquals(1, account.getMachucvu());
    assertEquals("admin", account.getTendangnhap());
  }

  @Test
  void loginSupportsShortUsernameAndDoesNotAuthenticateNonAdmin() {
    DangNhapModel credentials = new DangNhapModel("admin", "secret");
    BeanPropertyBindingResult binding = new BeanPropertyBindingResult(credentials, "dangnhapadmin");
    ModelMap model = new ModelMap();
    when(nhanVienService.getAdminByCredentials("admin", "secret")).thenReturn(null);

    assertEquals("admin/login_admin", controller.dangNhapProcess(credentials, binding, model));
    assertTrue(model.containsAttribute("resultsAdmin"));
    verify(nhanVienService).getAdminByCredentials("admin", "secret");
  }

  @Test
  void logoutInvalidatesSessionAndRedirectsToAdminLogin() {
    MockHttpSession session = sessionWithRole(1);
    assertEquals("redirect:/admin/dangnhap", controller.logout(session, new ExtendedModelMap()));
    assertTrue(session.isInvalid());
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }
}

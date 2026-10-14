package com.banhang.controller.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChucVu;
import com.banhang.entity.HoaDon;
import com.banhang.entity.LoaiYeuCauDoiTra;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangKyModel;
import com.banhang.model.DangNhapModel;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.ChucVuService;
import com.banhang.service.HoaDonService;
import com.banhang.service.KhoService;
import com.banhang.service.NhanVienService;
import com.banhang.service.YeuCauDoiTraService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@ExtendWith(MockitoExtension.class)
class TaiKhoanUserControllerTest {
  @Mock private JavaMailSender mailSender;
  @Mock private NhanVienService nhanVienService;
  @Mock private ChucVuService chucVuService;
  @Mock private HoaDonService hoaDonService;
  @Mock private KhoService khoService;
  @Mock private ChiTietSanPhamService chiTietSanPhamService;
  @Mock private YeuCauDoiTraService yeuCauDoiTraService;

  private TaiKhoanUserController controller;

  @BeforeEach
  void setUp() {
    controller =
        new TaiKhoanUserController(
            mailSender,
            nhanVienService,
            chucVuService,
            hoaDonService,
            khoService,
            chiTietSanPhamService,
            yeuCauDoiTraService);
    ReflectionTestUtils.setField(controller, "baseUrl", "http://localhost:8080");
  }

  @Test
  void loginPageRedirectsSignedInRolesAndShowsFormForGuest() {
    ExtendedModelMap model = new ExtendedModelMap();
    assertEquals("user/login_user", controller.showDangNhap(model, new MockHttpSession()));
    assertTrue(model.containsAttribute("dangnhapuser"));

    MockHttpSession adminSession = sessionWithRole(1);
    assertEquals("redirect:/admin", controller.showDangNhap(new ExtendedModelMap(), adminSession));
    assertEquals("redirect:/", controller.showDangNhap(new ExtendedModelMap(), sessionWithRole(3)));
  }

  @Test
  void loginRejectsUnknownUserAndCreatesSessionForValidUser() {
    DangNhapModel credentials = new DangNhapModel("customer@example.com", "secret");
    BeanPropertyBindingResult binding = new BeanPropertyBindingResult(credentials, "dangnhapuser");
    ModelMap model = new ModelMap();
    when(nhanVienService.getCustomerByCredentials("customer@example.com", "secret"))
        .thenReturn(null);

    assertEquals("user/login_user", controller.dangNhapProcess(credentials, binding, model));
    assertTrue(model.containsAttribute("resultDangNhap"));

    NhanVien employee = new NhanVien();
    employee.setManhanvien(12);
    employee.setHoten("Customer");
    employee.setEmail("customer@example.com");
    employee.setTendangnhap("customer@example.com");
    employee.setMatkhau("secret");
    employee.setChucVu(ChucVu.builder().machucvu(3).build());
    when(nhanVienService.getCustomerByCredentials("customer@example.com", "secret"))
        .thenReturn(employee);

    assertEquals("redirect:/", controller.dangNhapProcess(credentials, binding, model));
    TaiKhoanLogin account = (TaiKhoanLogin) model.get("taikhoan");
    assertEquals(12, account.getManhanvien());
    assertEquals(3, account.getMachucvu());
    assertEquals("customer@example.com", account.getTendangnhap());
  }

  @Test
  void loginRejectsEmployeeAccount() {
    DangNhapModel credentials = new DangNhapModel("staff@example.com", "secret");
    BeanPropertyBindingResult binding = new BeanPropertyBindingResult(credentials, "dangnhapuser");
    ModelMap model = new ModelMap();
    NhanVien employee = new NhanVien();
    employee.setChucVu(ChucVu.builder().machucvu(2).build());
    when(nhanVienService.getCustomerByCredentials("staff@example.com", "secret"))
        .thenReturn(employee);

    assertEquals("user/login_user", controller.dangNhapProcess(credentials, binding, model));
    assertTrue(model.containsAttribute("resultDangNhap"));
    assertFalse(model.containsAttribute("taikhoan"));
  }

  @Test
  void registrationRejectsPasswordMismatchWithoutPersisting() throws Exception {
    DangKyModel registration = validRegistration();
    registration.setNhaplaimkdk("different");
    ModelMap model = new ModelMap();
    BeanPropertyBindingResult binding = new BeanPropertyBindingResult(registration, "dangkyuser");

    assertEquals("user/regester_user", controller.dangKyProcess(registration, binding, model));
    assertTrue(model.containsAttribute("error_nhaplaimatkhaudk"));
    verify(nhanVienService, never()).register(any(NhanVien.class));
  }

  @Test
  void registrationTrimsFieldsAndPersistsCustomerRole() throws Exception {
    DangKyModel registration = validRegistration();
    registration.setHotendk("  Customer Name  ");
    registration.setEmaildk(" customer@example.com ");
    registration.setDiachidk("  Main Street  ");
    ModelMap model = new ModelMap();
    BeanPropertyBindingResult binding = new BeanPropertyBindingResult(registration, "dangkyuser");
    when(nhanVienService.checkEmail("customer@example.com")).thenReturn(false);
    when(chucVuService.getById(3)).thenReturn(ChucVu.builder().machucvu(3).build());
    when(nhanVienService.register(any(NhanVien.class))).thenReturn(25);
    when(mailSender.createMimeMessage())
        .thenReturn(new MimeMessage(Session.getInstance(new Properties())));

    assertEquals("user/regester_user", controller.dangKyProcess(registration, binding, model));
    assertTrue(model.get("success_fail").toString().contains("Đăng ký thành công"));

    ArgumentCaptor<NhanVien> captor = ArgumentCaptor.forClass(NhanVien.class);
    verify(nhanVienService).register(captor.capture());
    NhanVien saved = captor.getValue();
    assertEquals("Customer Name", saved.getHoten());
    assertEquals("customer@example.com", saved.getEmail());
    assertEquals("Main Street", saved.getDiachi());
    assertEquals("secret1", saved.getMatkhau());
    assertEquals(3, saved.getChucVu().getMachucvu());
    assertFalse(saved.getLock());
    assertFalse(saved.getXacthuc());
    assertEquals(64, saved.getTokenXacThuc().length());
    assertTrue(saved.getThoiHanXacThuc().isAfter(java.time.LocalDateTime.now()));
    verify(mailSender).send(any(MimeMessage.class));
  }

  @Test
  void verificationEndpointAcceptsTokenAndRejectsMissingToken() {
    when(nhanVienService.verifyEmailToken(eq("valid-token"), any())).thenReturn(true);
    ExtendedModelMap model = new ExtendedModelMap();

    assertEquals("user/xac_thuc_email_user", controller.verifyEmail("valid-token", model));
    assertTrue(model.get("verificationResult").toString().contains("thành công"));
    assertEquals("user/xac_thuc_email_user", controller.verifyEmail(null, model));
    assertTrue(model.get("verificationResult").toString().contains("không hợp lệ"));
    verify(nhanVienService).verifyEmailToken(eq("valid-token"), any());
  }

  @Test
  void logoutInvalidatesSessionAndRedirectsHome() {
    MockHttpSession session = sessionWithRole(3);

    assertEquals("redirect:/", controller.logout(session, new ExtendedModelMap()));
    assertTrue(session.isInvalid());
  }

  @Test
  void accountPageAndOrderHistoryRequireCustomerAndLoadOwnData() {
    MockHttpSession guest = new MockHttpSession();
    assertEquals("redirect:/dangnhap", controller.accountPage(guest, new ExtendedModelMap()));
    assertEquals("redirect:/dangnhap", controller.orderHistory(guest, new ExtendedModelMap()));

    MockHttpSession session = customerSession();
    NhanVien customer = new NhanVien();
    customer.setManhanvien(12);
    when(nhanVienService.getInforbyId(12)).thenReturn(customer);
    ExtendedModelMap accountModel = new ExtendedModelMap();
    assertEquals("user/account_user", controller.accountPage(session, accountModel));
    assertSame(customer, accountModel.get("customer"));

    List<HoaDon> orders = List.of(new HoaDon());
    when(hoaDonService.getListHoaDonByEmail("customer@example.com")).thenReturn(orders);
    ExtendedModelMap orderModel = new ExtendedModelMap();
    assertEquals("user/orders_user", controller.orderHistory(session, orderModel));
    assertSame(orders, orderModel.get("orders"));
  }

  @Test
  void customerCanChangePasswordOnlyWithValidCurrentPasswordAndMatchingConfirmation() {
    MockHttpSession session = customerSession();
    RedirectAttributesModelMap invalidModel = new RedirectAttributesModelMap();
    assertEquals(
        "redirect:/tai-khoan",
        controller.changePassword("oldPassword", "newPassword", "mismatch", session, invalidModel));
    assertTrue(invalidModel.getFlashAttributes().containsKey("accountError"));
    verify(nhanVienService, never()).changeCustomerPassword(anyInt(), any(), any());

    RedirectAttributesModelMap validModel = new RedirectAttributesModelMap();
    when(nhanVienService.changeCustomerPassword(12, "oldPassword", "newPassword")).thenReturn(true);
    assertEquals(
        "redirect:/tai-khoan",
        controller.changePassword(
            "oldPassword", "newPassword", "newPassword", session, validModel));
    assertTrue(validModel.getFlashAttributes().containsKey("accountResult"));
    verify(nhanVienService).changeCustomerPassword(12, "oldPassword", "newPassword");
  }

  @Test
  void customerCanViewOnlyTheirOwnOrderAndCancelOnlyViaCustomerService() {
    MockHttpSession session = customerSession();
    NhanVien customer = new NhanVien();
    customer.setEmail("customer@example.com");
    when(nhanVienService.getInforbyId(12)).thenReturn(customer);
    HoaDon order = new HoaDon();
    when(hoaDonService.getCustomerOrderById(51, "customer@example.com")).thenReturn(order);

    ExtendedModelMap model = new ExtendedModelMap();
    assertEquals("user/order-details_user", controller.orderDetails(51, session, model));
    assertSame(order, model.get("order"));
    when(hoaDonService.getCustomerOrderById(52, "customer@example.com")).thenReturn(null);
    assertEquals("404", controller.orderDetails(52, session, new ExtendedModelMap()));

    RedirectAttributesModelMap cancelModel = new RedirectAttributesModelMap();
    assertEquals("redirect:/don-hang", controller.cancelOrder(51, session, cancelModel));
    verify(khoService).cancelCustomerOrder(51, "customer@example.com");
    assertTrue(cancelModel.getFlashAttributes().containsKey("orderResult"));
  }

  @Test
  void customerCanSubmitReturnRequestForOwnedOrder() {
    MockHttpSession session = customerSession();
    NhanVien customer = new NhanVien();
    customer.setEmail("customer@example.com");
    when(nhanVienService.getInforbyId(12)).thenReturn(customer);
    HoaDon order = new HoaDon();
    order.setMahoadon(51);
    order.setChiTietHoaDon(java.util.Set.of());
    when(hoaDonService.getCustomerOrderById(51, "customer@example.com")).thenReturn(order);
    when(yeuCauDoiTraService.isEligible(order)).thenReturn(true);

    assertEquals(
        "user/return-request_user",
        controller.showReturnRequest(51, session, new org.springframework.ui.ExtendedModelMap()));

    RedirectAttributesModelMap model = new RedirectAttributesModelMap();
    assertEquals(
        "redirect:/don-hang/51",
        controller.createReturnRequest(
            51, "TRA", "Sản phẩm bị lỗi", List.of(20), List.of(1), List.of(""), session, model));
    assertTrue(model.getFlashAttributes().containsKey("orderResult"));
    verify(yeuCauDoiTraService)
        .createRequest(
            51,
            "customer@example.com",
            LoaiYeuCauDoiTra.TRA,
            "Sản phẩm bị lỗi",
            List.of(new YeuCauDoiTraService.RequestLine(20, 1, null)));
  }

  @Test
  void updateAccountTrimsAndPersistsProfileFields() {
    MockHttpSession session = customerSession();
    NhanVien customer = new NhanVien();
    customer.setManhanvien(12);
    when(nhanVienService.getInforbyId(12)).thenReturn(customer);
    when(nhanVienService.updateInfor(customer)).thenReturn(true);
    ExtendedModelMap model = new ExtendedModelMap();

    assertEquals(
        "user/account_user",
        controller.updateAccount("  Customer Name ", " Main Street  ", session, model));

    assertEquals("Customer Name", customer.getHoten());
    assertEquals("Main Street", customer.getDiachi());
    assertEquals("Customer Name", ((TaiKhoanLogin) session.getAttribute("taikhoan")).getHoten());
    assertTrue(model.containsAttribute("accountResult"));
  }

  private MockHttpSession customerSession() {
    MockHttpSession session = sessionWithRole(3);
    TaiKhoanLogin account = (TaiKhoanLogin) session.getAttribute("taikhoan");
    account.setManhanvien(12);
    account.setTendangnhap("customer@example.com");
    account.setHoten("Customer");
    return session;
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }

  private DangKyModel validRegistration() {
    DangKyModel registration = new DangKyModel();
    registration.setHotendk("Customer Name");
    registration.setEmaildk("customer@example.com");
    registration.setDiachidk("Main Street");
    registration.setMatkhaudk("secret1");
    registration.setNhaplaimkdk("secret1");
    registration.setGioitinhdk("0");
    return registration;
  }
}

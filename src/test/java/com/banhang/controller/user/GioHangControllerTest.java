package com.banhang.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.GioHang;
import com.banhang.entity.MauSanPham;
import com.banhang.entity.SanPham;
import com.banhang.entity.SizeSanPham;
import com.banhang.service.ChiTietHoaDonService;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.GioHangService;
import com.banhang.service.HoaDonService;
import com.banhang.service.KhoService;
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
import org.springframework.ui.ModelMap;

@ExtendWith(MockitoExtension.class)
class GioHangControllerTest {
  @Mock private GioHangService gioHangService;
  @Mock private HoaDonService hoaDonService;
  @Mock private ChiTietHoaDonService chiTietHoaDonService;
  @Mock private ChiTietSanPhamService chiTietSanPhamService;
  @Mock private KhoService khoService;
  @Mock private JavaMailSender mailSender;

  private GioHangController controller;

  @BeforeEach
  void setUp() {
    controller =
        new GioHangController(
            gioHangService,
            hoaDonService,
            chiTietHoaDonService,
            chiTietSanPhamService,
            khoService,
            mailSender);
  }

  @Test
  void addItemPassesRequestDataToServiceAndReturnsSuccess() {
    MockHttpSession session = new MockHttpSession();
    List<GioHang> cart = List.of(new GioHang());
    when(chiTietSanPhamService.getInforCTSanPhambyID(1))
        .thenReturn(List.of(variant(10, 1, 2, 3, 10)));
    when(gioHangService.getCart(session)).thenReturn(cart);
    ModelMap model = new ModelMap();

    String result =
        controller.themgiohang(10, 1, 2, 3, "Shirt", "120", "Blue", "M", 2, session, model);

    assertEquals("true", result);
    assertEquals(cart, model.get("giohang"));
    verify(gioHangService).addItem(10, 1, 2, 3, "Shirt", "120", "Blue", "M", 2, session);
  }

  @Test
  void cartPageRedirectsAdminAndDisplaysCartForCustomer() {
    MockHttpSession adminSession = sessionWithRole(1);
    assertEquals("redirect:/admin", controller.loadGioHang(adminSession, new ModelMap()));
    verifyNoInteractions(gioHangService);

    MockHttpSession customerSession = sessionWithRole(3);
    List<GioHang> cart = List.of(new GioHang());
    when(gioHangService.hasCart(customerSession)).thenReturn(true);
    when(gioHangService.getCart(customerSession)).thenReturn(cart);
    ModelMap model = new ModelMap();

    assertEquals("cart_user", controller.loadGioHang(customerSession, model));
    assertEquals(false, model.get("isEmpty"));
    assertEquals(1, model.get("soluonggiohang"));
    assertEquals(cart, model.get("gioHangs"));
  }

  @Test
  void deleteAndUpdateEndpointsDelegateToService() {
    MockHttpSession session = new MockHttpSession();
    GioHang item = new GioHang();
    item.setMasp(1);
    item.setMasize(2);
    item.setMamau(3);
    item.setMachitietsanpham(10);
    when(gioHangService.getCart(session)).thenReturn(List.of(item));
    when(chiTietSanPhamService.getInforCTSanPhambyID(1))
        .thenReturn(List.of(variant(10, 1, 2, 3, 10)));

    assertEquals("true", controller.xoaSanPhamKhoiGio(1, 3, 2, session, new ModelMap()));
    assertEquals("true", controller.upadateSanPhamKhoiGio(1, 2, 3, 4, session, new ModelMap()));

    verify(gioHangService).removeItem(1, 2, 3, session);
    verify(gioHangService).updateQuantity(1, 2, 3, 4, session);
  }

  @Test
  void checkoutSavesInvoiceDetailsWithInvoiceIdAndQuantity() throws Exception {
    MockHttpSession session = new MockHttpSession();
    GioHang item = new GioHang();
    item.setMachitietsanpham(17);
    item.setMasp(2);
    item.setMasize(3);
    item.setMamau(4);
    item.setTensp("Shirt");
    item.setTensize("M");
    item.setTenmau("Blue");
    item.setGiatien("120");
    item.setSoluong(2);
    session.setAttribute("giohang", List.of(item));

    when(hoaDonService.addNewHoaDonUser(any())).thenReturn(51);
    when(gioHangService.getCart(session)).thenReturn(List.of(item));
    ChiTietSanPham inventory = variant(17, 2, 3, 4, 10);
    when(chiTietSanPhamService.getInforCTSanPhambyID(2)).thenReturn(List.of(inventory));
    when(chiTietHoaDonService.addChiTietHoaDon(any())).thenReturn(1);
    when(chiTietSanPhamService.updateCTSanPham(inventory)).thenReturn(true);
    when(mailSender.createMimeMessage())
        .thenReturn(new MimeMessage(Session.getInstance(new Properties())));

    assertEquals(
        "true",
        controller.themHoaDon(
            "Customer", "customer@example.com", "0912345678", "0", "Street", "", session));

    ArgumentCaptor<ChiTietHoaDon> captor = ArgumentCaptor.forClass(ChiTietHoaDon.class);
    verify(chiTietHoaDonService).addChiTietHoaDon(captor.capture());
    ChiTietHoaDon detail = captor.getValue();
    assertEquals(51, detail.getChiTietHoaDonId().getMahoadon());
    assertEquals(17, detail.getChiTietHoaDonId().getMachitietsanpham());
    assertEquals(2, detail.getSoluong());
    assertEquals("120", detail.getGiatien());
    verify(mailSender).send(any(MimeMessage.class));
  }

  @Test
  void checkoutReportsEmailFailureAfterSavingOrderAndClearsCart() throws Exception {
    MockHttpSession session = new MockHttpSession();
    GioHang item = new GioHang();
    item.setMachitietsanpham(17);
    item.setMasp(2);
    item.setMasize(3);
    item.setMamau(4);
    item.setTensp("Shirt");
    item.setTensize("M");
    item.setTenmau("Blue");
    item.setGiatien("120");
    item.setSoluong(1);
    session.setAttribute("giohang", List.of(item));

    when(hoaDonService.addNewHoaDonUser(any())).thenReturn(52);
    when(gioHangService.getCart(session)).thenReturn(List.of(item));
    ChiTietSanPham inventory = variant(17, 2, 3, 4, 10);
    when(chiTietSanPhamService.getInforCTSanPhambyID(2)).thenReturn(List.of(inventory));
    when(chiTietHoaDonService.addChiTietHoaDon(any())).thenReturn(1);
    when(chiTietSanPhamService.updateCTSanPham(inventory)).thenReturn(true);
    when(mailSender.createMimeMessage())
        .thenReturn(new MimeMessage(Session.getInstance(new Properties())));
    doThrow(new org.springframework.mail.MailSendException("SMTP unavailable"))
        .when(mailSender)
        .send(any(MimeMessage.class));

    assertEquals(
        "email-failed",
        controller.themHoaDon(
            "Customer", "customer@example.com", "0912345678", "0", "Street", "", session));
    assertNull(session.getAttribute("giohang"));
    verify(hoaDonService).addNewHoaDonUser(any());
    verify(chiTietHoaDonService).addChiTietHoaDon(any());
    verify(chiTietSanPhamService).updateCTSanPham(inventory);
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }

  private ChiTietSanPham variant(
      int detailId, int productId, int sizeId, int colorId, int inventory) {
    SanPham product = new SanPham();
    product.setMasanpham(productId);
    product.setTensanpham("Shirt");
    product.setGiatien("120");
    product.setHiden(false);
    SizeSanPham size = new SizeSanPham();
    size.setMasize(sizeId);
    size.setSize("M");
    MauSanPham color = new MauSanPham();
    color.setMamau(colorId);
    color.setTenmau("Blue");
    ChiTietSanPham variant = new ChiTietSanPham();
    variant.setMachitietsanpham(detailId);
    variant.setSanPham(product);
    variant.setSizeSanPham(size);
    variant.setMauSanPham(color);
    variant.setSoluong(inventory);
    return variant;
  }
}

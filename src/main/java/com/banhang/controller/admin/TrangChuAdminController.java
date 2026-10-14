package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.service.HoaDonService;
import com.banhang.service.SanPhamService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin")
@SessionAttributes({"taikhoan"})
@RequiredArgsConstructor
public class TrangChuAdminController {
  private final SanPhamService sanPhamService;
  private final HoaDonService hoaDonService;

  @GetMapping
  public String index(ModelMap map, HttpSession httpSession) {
    if (checkSecurity(httpSession)) {
      var products = sanPhamService.getAllSanPham();
      var invoices = hoaDonService.getAllHoaDon();
      map.addAttribute("productCount", products.size());
      map.addAttribute(
          "pendingInvoiceCount",
          invoices.stream().filter(invoice -> invoice.getTinhtrang() == 0).count());
      map.addAttribute(
          "completedInvoiceCount",
          invoices.stream().filter(invoice -> invoice.getTinhtrang() == 3).count());
      map.addAttribute("recentInvoices", invoices.stream().limit(5).toList());
      return "admin/index_admin";
    }
    return httpSession.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
  }

  @GetMapping("/reports")
  public String reports(HttpSession httpSession) {
    if (checkSecurity(httpSession)) {
      return "admin/reports_admin";
    }
    return httpSession.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
  }

  // admin -> tru  // other  -> false
  public boolean checkSecurity(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      return taiKhoanLogin.getMachucvu() == 1;
    }
    return false;
  }
}

package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/inventory")
public class KhoAdminPageController {
  @GetMapping
  public String inventory(HttpSession session) {
    Object account = session.getAttribute("taikhoan");
    if (account instanceof TaiKhoanLogin login && login.getMachucvu() == 1) {
      return "admin/inventory_admin";
    }
    return account == null ? "redirect:/admin/dangnhap" : "401";
  }
}

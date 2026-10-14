package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.HoaDon;
import com.banhang.entity.TrangThaiHoaDon;
import com.banhang.service.HoaDonService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin/orders")
@SessionAttributes("taikhoan")
@RequiredArgsConstructor
public class QuanLyHoaDonAdminController {
  private final HoaDonService hoaDonService;

  @GetMapping
  public String listOrders(ModelMap model, HttpSession session) {
    if (!isAdmin(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    model.addAttribute("orders", hoaDonService.getAllHoaDon());
    return "admin/MG_order_admin";
  }

  @PostMapping("/{id}/status")
  public String updateStatus(
      @PathVariable int id, @RequestParam int status, ModelMap model, HttpSession session) {
    if (!isAdmin(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }

    HoaDon order = hoaDonService.getHoaDonById(id);
    if (order == null
        || status != order.getTinhtrang() + 1
        || status > TrangThaiHoaDon.HOAN_THANH.getCode()) {
      model.addAttribute("orderError", "Không tìm thấy đơn hàng hoặc trạng thái không hợp lệ.");
    } else {
      order.setTinhtrang(status);
      if (status == TrangThaiHoaDon.HOAN_THANH.getCode()) {
        order.setCompletedAt(LocalDateTime.now());
      }
      model.addAttribute(
          "orderResult",
          hoaDonService.updateHoaDon(order)
              ? "Đã cập nhật trạng thái đơn hàng."
              : "Không thể cập nhật trạng thái đơn hàng.");
    }
    model.addAttribute("orders", hoaDonService.getAllHoaDon());
    return "admin/MG_order_admin";
  }

  private boolean isAdmin(HttpSession session) {
    Object account = session.getAttribute("taikhoan");
    return account instanceof TaiKhoanLogin login && login.getMachucvu() == 1;
  }
}

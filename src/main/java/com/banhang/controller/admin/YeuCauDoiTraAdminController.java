package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.TrangThaiYeuCauDoiTra;
import com.banhang.service.YeuCauDoiTraService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/doitra")
@RequiredArgsConstructor
public class YeuCauDoiTraAdminController {
  private final YeuCauDoiTraService yeuCauDoiTraService;

  @GetMapping
  public String listRequests(HttpSession session, ModelMap model) {
    if (!isAdmin(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    model.addAttribute("returnRequests", yeuCauDoiTraService.getAllRequests());
    return "admin/return-requests_admin";
  }

  @PostMapping("/{id}/trang-thai")
  public String updateRequestStatus(
      @PathVariable long id, @RequestParam String trangThai, HttpSession session, ModelMap model) {
    if (!isAdmin(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    try {
      yeuCauDoiTraService.updateRequestStatus(id, TrangThaiYeuCauDoiTra.valueOf(trangThai));
      model.addAttribute("returnResult", "Đã cập nhật trạng thái yêu cầu.");
    } catch (IllegalArgumentException exception) {
      model.addAttribute("returnError", exception.getMessage());
    }
    model.addAttribute("returnRequests", yeuCauDoiTraService.getAllRequests());
    return "admin/return-requests_admin";
  }

  private boolean isAdmin(HttpSession session) {
    Object account = session.getAttribute("taikhoan");
    return account instanceof TaiKhoanLogin login && login.getMachucvu() == 1;
  }
}

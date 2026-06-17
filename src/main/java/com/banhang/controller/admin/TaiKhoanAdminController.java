package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangNhapModel;
import com.banhang.service.NhanVienService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controller class quản lý việc đăng nhập, đăng xuất tài khoản admin.
 *
 * @author System
 * @version 1.0
 */
@Controller
@SessionAttributes({"taikhoan"})
@RequestMapping("/admin")
@RequiredArgsConstructor
public class TaiKhoanAdminController {
  private final NhanVienService nhanVienService;

  @GetMapping("dangnhap")
  public String showDangNhap(Model m, HttpSession httpSession) {
    int role = checkSecurityUser(httpSession);

    // 1 -> admin  !! other exepted 0 -> user  !! 0 no login
    if (role == 1) {
      return "redirect:/admin";
    } else if (role != 0) {
      return "redirect:/";
    } else {
      m.addAttribute("dangnhapadmin", new DangNhapModel());
      return "login_admin";
    }
  }

  @PostMapping("dangnhap")
  public String dangNhapProcess(
      @Valid @ModelAttribute("dangnhapadmin") DangNhapModel dangnhapadmin,
      BindingResult br,
      ModelMap map) {
    if (br.hasErrors()) {
      return "login_admin";
    }

    boolean isAdmin =
        nhanVienService.checkAdmin(dangnhapadmin.getEmail(), dangnhapadmin.getMatKhau());
    if (isAdmin) {
      NhanVien tk =
          nhanVienService.getInforby_User_Pass(
              dangnhapadmin.getEmail(), dangnhapadmin.getMatKhau());
      if (tk != null) {
        TaiKhoanLogin taikhoan = new TaiKhoanLogin();
        taikhoan.setHoten(tk.getHoten());
        taikhoan.setMachucvu(tk.getChucVu().getMachucvu());
        taikhoan.setManhanvien(tk.getManhanvien());
        taikhoan.setTendangnhap(tk.getEmail());
        taikhoan.setMatkhau(tk.getMatkhau());
        map.addAttribute("taikhoan", taikhoan);
        return "redirect:/admin";
      }
    }
    map.addAttribute(
        "resultsAdmin", "Bạn không đủ quyền để truy cập hoặc thông tin đăng nhập không đúng");
    return "login_admin";
  }

  @GetMapping("dangxuat")
  public String logout(HttpSession httpSession, Model model) {
    httpSession.removeAttribute("taikhoan");
    httpSession.invalidate();
    if (model.containsAttribute("taikhoan")) {
      model.asMap().remove("taikhoan");
    }
    return "redirect:/admin/dangnhap";
  }

  // admin -> true  // other  -> false
  public boolean checkSecurity(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    return taiKhoanLogin != null && taiKhoanLogin.getMachucvu() == 1;
  }

  // admin -> 1  // other  -> 0
  public int checkSecurityUser(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      return taiKhoanLogin.getMachucvu();
    }
    return 0;
  }
}

package com.banhang.controller.user;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.entity.SanPham;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.SanPhamService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
@RequestMapping("/")
@RequiredArgsConstructor
public class TrangChuUserController {
  private final DanhMucSanPhamService danhMucSanPhamService;
  private final SanPhamService sanPhamService;

  // @SessionAttribute("taikhoan") TaiKhoanLogin taikhoan
  @GetMapping
  @Transactional
  public String Default(ModelMap map, HttpSession httpSession) {
    int temp = checkSecurityUser(httpSession);
    if (temp == 1) {
      return "redirect:/admin";
    } else {
      // get list Danh muc San Pham
      List<DanhMucSanPham> showListDanhMucSP = danhMucSanPhamService.getAllDanhMucSanPham();

      // get list San Pham
      List<SanPham> showListSanPham = sanPhamService.getAllSanPham();
      map.addAttribute("showListDanhMucSP", showListDanhMucSP);
      map.addAttribute("showListSanPham", showListSanPham);

      return "index_user";
    }
  }

  @GetMapping("chi-tiet-san-pham/{id}")
  public String chitietsanpham(@PathVariable("id") String id, ModelMap map) {
    int masanpham = Integer.parseInt(id);
    SanPham sanphamRS = sanPhamService.getListSanPhamById(masanpham);
    List<DanhMucSanPham> showListDanhMucSP = danhMucSanPhamService.getAllDanhMucSanPham();
    map.addAttribute("sanphamRS", sanphamRS);
    map.addAttribute("showListDanhMucSP", showListDanhMucSP);

    return "product-details_user";
  }

  public int checkSecurityUser(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      return taiKhoanLogin.getMachucvu();
    }
    return 0;
  }
}

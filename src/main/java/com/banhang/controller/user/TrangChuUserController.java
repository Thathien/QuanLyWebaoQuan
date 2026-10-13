package com.banhang.controller.user;

import com.banhang.commons.ProductImageSupport;
import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.entity.SanPham;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.SanPhamService;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
@RequestMapping("/")
@RequiredArgsConstructor
public class TrangChuUserController {
  private static final int PRODUCTS_PER_PAGE = 9;

  private final DanhMucSanPhamService danhMucSanPhamService;
  private final SanPhamService sanPhamService;

  // @SessionAttribute("taikhoan") TaiKhoanLogin taikhoan
  @GetMapping
  @Transactional
  public String Default(
      @RequestParam(required = false) Integer category,
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(defaultValue = "0") int page,
      ModelMap map,
      HttpSession httpSession) {
    int temp = checkSecurityUser(httpSession);
    if (temp == 1) {
      return "redirect:/admin";
    } else {
      if ((category != null && category < 1)
          || (minPrice != null && minPrice.signum() < 0)
          || (maxPrice != null && maxPrice.signum() < 0)
          || (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0)) {
        throw new IllegalArgumentException("Bộ lọc danh mục hoặc khoảng giá không hợp lệ.");
      }
      // get list Danh muc San Pham
      List<DanhMucSanPham> showListDanhMucSP = danhMucSanPhamService.getAllDanhMucSanPham();

      // get list San Pham
      String normalizedQuery = query == null ? "" : query.trim();
      Page<SanPham> productPage =
          sanPhamService.getStorefrontProducts(
              category,
              normalizedQuery,
              minPrice,
              maxPrice,
              PageRequest.of(Math.max(page, 0), PRODUCTS_PER_PAGE));
      List<SanPham> showListSanPham =
          productPage.getContent().stream()
              .peek(
                  product ->
                      product.setHinhsanpham(
                          ProductImageSupport.safeFileName(product.getHinhsanpham())))
              .toList();
      map.addAttribute("showListDanhMucSP", showListDanhMucSP);
      map.addAttribute("showListSanPham", showListSanPham);
      map.addAttribute("productPage", productPage);
      map.addAttribute("selectedCategory", category);
      map.addAttribute("searchQuery", normalizedQuery);
      map.addAttribute("minPrice", minPrice);
      map.addAttribute("maxPrice", maxPrice);

      return "index_user";
    }
  }

  @GetMapping("chi-tiet-san-pham/{id}")
  public String chitietsanpham(@PathVariable("id") int masanpham, ModelMap map) {
    SanPham sanphamRS = sanPhamService.getListSanPhamById(masanpham);
    if (sanphamRS == null || !Boolean.FALSE.equals(sanphamRS.getHiden())) {
      return "404";
    }
    sanphamRS.setHinhsanpham(ProductImageSupport.safeFileName(sanphamRS.getHinhsanpham()));
    List<DanhMucSanPham> showListDanhMucSP = danhMucSanPhamService.getAllDanhMucSanPham();
    map.addAttribute("sanphamRS", sanphamRS);
    map.addAttribute("productImageName", sanphamRS.getHinhsanpham());
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

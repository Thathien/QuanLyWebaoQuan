package com.banhang.controller.admin;

import com.banhang.commons.ProductImageSupport;
import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.entity.MauSanPham;
import com.banhang.entity.SanPham;
import com.banhang.entity.SizeSanPham;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.HoaDonService;
import com.banhang.service.KhuyenMaiService;
import com.banhang.service.MauSanPhamService;
import com.banhang.service.NhanVienService;
import com.banhang.service.SanPhamService;
import com.banhang.service.SizeSanPhamService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@SessionAttributes({"taikhoan"})
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class QuanLyAdminController {
  private final ServletContext context;
  private final SanPhamService sanPhamService;
  private final ChiTietSanPhamService chiTietSanPhamServie;
  private final DanhMucSanPhamService danhMucSanPhamService;
  private final HoaDonService hoDonService;
  private final KhuyenMaiService khuyenMaiSerVice;
  private final MauSanPhamService mauSanPhamService;
  private final SizeSanPhamService sizeSanPhamService;
  private final NhanVienService nhanVienService;

  @GetMapping()
  public String Default(ModelMap modelMap, HttpSession httpSession) {
    if (!checkSecurity(httpSession)) {
      return httpSession.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    populateProductPage(modelMap);
    return "admin/products_admin";
  }

  @GetMapping("/detal/{id}")
  public String chitietsanpham(@PathVariable("id") int id, ModelMap map, HttpSession httpSession) {
    if (checkSecurity(httpSession)) {
      SanPham sanphamRS = sanPhamService.getListSanPhamById(id);
      if (sanphamRS == null) {
        return "404";
      }
      populateProductPage(map);
      map.addAttribute("sanphamRS", sanphamRS);
      map.addAttribute("editingProduct", true);
      return "admin/products_admin";
    } else {
      return "401";
    }
  }

  @PostMapping("/delete")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> xoasanpham(
      @RequestParam String listIdSanPham, HttpSession httpSession) {
    if (!checkSecurity(httpSession)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(bulkDeleteResponse(List.of(), Map.of("request", "Vui lòng đăng nhập.")));
    }
    if (listIdSanPham == null || listIdSanPham.isBlank()) {
      return ResponseEntity.badRequest()
          .body(bulkDeleteResponse(List.of(), Map.of("request", "Chưa chọn sản phẩm.")));
    }
    List<Integer> deletedIds = new ArrayList<>();
    Map<String, String> failedIds = new LinkedHashMap<>();
    for (String idStr : listIdSanPham.trim().split(",")) {
      int productId;
      try {
        productId = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        failedIds.put(idStr, "Mã sản phẩm không hợp lệ.");
        continue;
      }
      if (productId <= 0) {
        failedIds.put(idStr, "Mã sản phẩm không hợp lệ.");
        continue;
      }
      try {
        SanPhamService.ProductDeleteStatus status =
            sanPhamService.deleteProductIfUnreferenced(productId);
        switch (status) {
          case DELETED -> deletedIds.add(productId);
          case NOT_FOUND -> failedIds.put(idStr, "Không tìm thấy sản phẩm.");
          case HAS_HISTORY ->
              failedIds.put(idStr, "Sản phẩm có lịch sử bán/nhập hoặc tồn kho; hãy ẩn sản phẩm.");
          case DELETE_FAILED -> failedIds.put(idStr, "Không thể xóa sản phẩm.");
        }
      } catch (DataIntegrityViolationException exception) {
        failedIds.put(idStr, "Sản phẩm đang được dữ liệu khác sử dụng; hãy ẩn sản phẩm.");
      }
    }
    return ResponseEntity.ok(bulkDeleteResponse(deletedIds, failedIds));
  }

  private Map<String, Object> bulkDeleteResponse(
      List<Integer> deletedIds, Map<String, String> failedIds) {
    return Map.of("deletedIds", deletedIds, "failedIds", failedIds);
  }

  @PostMapping("/update")
  public String updatesanpham(@RequestParam int masanpham, ModelMap map, HttpSession httpSession) {
    if (checkSecurity(httpSession)) {
      SanPham sanPhamUpdate = sanPhamService.getListSanPhamById(masanpham);
      if (sanPhamUpdate == null) {
        return "404";
      }
      populateProductPage(map);
      map.addAttribute("sanphamRS", sanPhamUpdate);
      map.addAttribute("editingProduct", true);
      return "admin/products_admin";
    } else {
      return "401";
    }
  }

  @PostMapping("addimage")
  @ResponseBody
  public String addImage(MultipartHttpServletRequest request, HttpSession session) {
    if (!checkSecurity(session)) {
      return "false";
    }
    var fileNames = request.getFileNames();
    if (fileNames == null || !fileNames.hasNext()) {
      return "false";
    }
    MultipartFile file = request.getFile(fileNames.next());
    try {
      return storeImage(file);
    } catch (java.io.IOException e) {
      throw new IllegalStateException("Không thể lưu ảnh sản phẩm.", e);
    }
  }

  @PostMapping("/save")
  @Transactional
  public String saveProduct(
      @RequestParam(required = false) Integer masanpham,
      @RequestParam String tensanpham,
      @RequestParam int madanhmucsanpham,
      @RequestParam String giatien,
      @RequestParam String mota,
      @RequestParam String doituong,
      @RequestParam(required = false) String hinhsanpham,
      @RequestParam(required = false) MultipartFile imageFile,
      @RequestParam(required = false) Integer masize,
      @RequestParam(required = false) Integer mamau,
      @RequestParam(required = false) Integer soluong,
      ModelMap model,
      HttpSession session) {
    if (!checkSecurity(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    SanPham product =
        masanpham == null ? new SanPham() : sanPhamService.getListSanPhamById(masanpham);
    if (product == null) {
      return "404";
    }
    String currentImage = product.getHinhsanpham();
    DanhMucSanPham category =
        danhMucSanPhamService.getAllDanhMucSanPham().stream()
            .filter(item -> item.getMadanhmucsanpham() == madanhmucsanpham)
            .findFirst()
            .orElse(null);
    SizeSanPham size =
        masize == null
            ? null
            : sizeSanPhamService.getAllSizeSanPham().stream()
                .filter(item -> item.getMasize() == masize)
                .findFirst()
                .orElse(null);
    MauSanPham color =
        mamau == null
            ? null
            : mauSanPhamService.getAllMauSanPham().stream()
                .filter(item -> item.getMamau() == mamau)
                .findFirst()
                .orElse(null);
    boolean validPrice = giatien.matches("\\d{1,20}") && new BigDecimal(giatien).signum() > 0;
    if (tensanpham.isBlank()
        || tensanpham.trim().length() > 100
        || mota.trim().length() > 500
        || !validPrice
        || doituong.isBlank()
        || category == null
        || (masanpham == null
            && (size == null || color == null || soluong == null || soluong < 0))) {
      populateProductPage(model);
      preserveProductForm(
          model,
          product,
          masanpham,
          tensanpham,
          category,
          giatien,
          mota,
          doituong,
          currentImage,
          masize,
          mamau,
          soluong);
      model.addAttribute("formError", "Thông tin sản phẩm chưa hợp lệ hoặc còn thiếu.");
      return "admin/products_admin";
    }

    product.setTensanpham(tensanpham.trim());
    product.setDanhMucSanPham(category);
    product.setGiatien(giatien.trim());
    product.setMota(mota.trim());
    product.setDoituong(doituong.trim());
    if (masanpham == null) {
      product.setHiden(false);
      if (sanPhamService.checkNameSanPhamBeforeAdd(tensanpham.trim())) {
        populateProductPage(model);
        preserveProductForm(
            model,
            product,
            masanpham,
            tensanpham,
            category,
            giatien,
            mota,
            doituong,
            currentImage,
            masize,
            mamau,
            soluong);
        model.addAttribute("formError", "Tên sản phẩm đã tồn tại.");
        return "admin/products_admin";
      }
    }
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        hinhsanpham = storeImage(imageFile);
      } catch (java.io.IOException e) {
        throw new IllegalStateException("Không thể lưu ảnh sản phẩm.", e);
      }
      if (hinhsanpham == null) {
        populateProductPage(model);
        preserveProductForm(
            model,
            product,
            masanpham,
            tensanpham,
            category,
            giatien,
            mota,
            doituong,
            currentImage,
            masize,
            mamau,
            soluong);
        model.addAttribute("formError", "Ảnh phải là JPG, PNG, GIF hoặc WebP và tối đa 5 MB.");
        return "admin/products_admin";
      }
    }
    product.setHinhsanpham(
        ProductImageSupport.safeFileName(
            hinhsanpham == null || hinhsanpham.isBlank() ? currentImage : hinhsanpham));

    boolean saved;
    if (masanpham == null) {
      int productId = sanPhamService.addSanPham(product);
      saved = productId > 0;
      if (saved) {
        ChiTietSanPham detail = new ChiTietSanPham();
        detail.setSanPham(product);
        detail.setSizeSanPham(size);
        detail.setMauSanPham(color);
        detail.setSoluong(soluong);
        detail.setNgaynhap(java.time.LocalDate.now().toString());
        saved = chiTietSanPhamServie.addCTSanPham(detail) > 0;
        if (!saved) {
          throw new IllegalStateException("Không thể lưu tồn kho ban đầu của sản phẩm.");
        }
      }
    } else {
      saved = sanPhamService.updateSanPham(product);
    }

    return saved ? "redirect:/admin/product?saved=true" : "redirect:/admin/product?error=save";
  }

  @PostMapping("/{id}/visibility")
  public String updateVisibility(
      @PathVariable int id, @RequestParam boolean hidden, HttpSession session) {
    if (!checkSecurity(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    SanPham product = sanPhamService.getListSanPhamById(id);
    if (product == null) {
      return "404";
    }
    product.setHiden(hidden);
    return sanPhamService.updateSanPham(product)
        ? "redirect:/admin/product?saved=true"
        : "redirect:/admin/product?error=save";
  }

  @PostMapping("/{id}/delete")
  public String deleteProduct(@PathVariable int id, HttpSession session) {
    if (!checkSecurity(session)) {
      return session.getAttribute("taikhoan") == null ? "redirect:/admin/dangnhap" : "401";
    }
    SanPhamService.ProductDeleteStatus result;
    try {
      result = sanPhamService.deleteProductIfUnreferenced(id);
    } catch (DataIntegrityViolationException exception) {
      return "redirect:/admin/product?error=delete";
    }
    if (result == SanPhamService.ProductDeleteStatus.NOT_FOUND) {
      return "404";
    }
    return result == SanPhamService.ProductDeleteStatus.DELETED
        ? "redirect:/admin/product?deleted=true"
        : "redirect:/admin/product?error=delete";
  }

  private void preserveProductForm(
      ModelMap model,
      SanPham product,
      Integer productId,
      String name,
      DanhMucSanPham category,
      String price,
      String description,
      String target,
      String currentImage,
      Integer sizeId,
      Integer colorId,
      Integer initialQuantity) {
    product.setMasanpham(productId);
    product.setTensanpham(name);
    product.setDanhMucSanPham(category);
    product.setGiatien(price);
    product.setMota(description);
    product.setDoituong(target);
    product.setHinhsanpham(currentImage);
    model.addAttribute("sanphamRS", product);
    model.addAttribute("editingProduct", productId != null);
    model.addAttribute("selectedSizeId", sizeId);
    model.addAttribute("selectedColorId", colorId);
    model.addAttribute("initialQuantity", initialQuantity);
  }

  private void populateProductPage(ModelMap model) {
    model.addAttribute("listSanPhamss", sanPhamService.getAllSanPham());
    model.addAttribute("danhMucSanPhams", danhMucSanPhamService.getAllDanhMucSanPham());
    model.addAttribute("mauSanPhams", mauSanPhamService.getAllMauSanPham());
    model.addAttribute("sizeSanPhams", sizeSanPhamService.getAllSizeSanPham());
  }

  private String storeImage(MultipartFile file) throws java.io.IOException {
    if (file == null || file.isEmpty() || file.getSize() > 5 * 1024 * 1024) {
      return null;
    }
    String originalName = file.getOriginalFilename();
    String extension =
        originalName == null || !originalName.contains(".")
            ? ""
            : originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    if (!List.of("jpg", "jpeg", "png", "gif", "webp").contains(extension)) {
      return null;
    }
    String directory = context.getRealPath("/resourcess/user/images/shop/");
    if (directory == null) {
      return null;
    }
    String storedName = UUID.randomUUID() + "." + extension;
    File destinationDirectory = new File(directory);
    if (!destinationDirectory.isDirectory() && !destinationDirectory.mkdirs()) {
      throw new java.io.IOException("Không thể tạo thư mục lưu ảnh.");
    }
    file.transferTo(new File(destinationDirectory, storedName));
    return storedName;
  }

  // admin -> tru  // other  -> false
  public boolean checkSecurity(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      if (taiKhoanLogin.getMachucvu() == 1) {
        return true;
      }
    }
    return false;
  }

  public static boolean isNumeric(String strNum) {
    return strNum != null && strNum.matches("\\d+");
  }
}

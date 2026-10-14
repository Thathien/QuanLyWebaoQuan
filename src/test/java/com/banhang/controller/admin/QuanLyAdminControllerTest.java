package com.banhang.controller.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;

@ExtendWith(MockitoExtension.class)
class QuanLyAdminControllerTest {
  @Mock private ServletContext servletContext;
  @Mock private SanPhamService sanPhamService;
  @Mock private ChiTietSanPhamService chiTietSanPhamService;
  @Mock private DanhMucSanPhamService danhMucSanPhamService;
  @Mock private HoaDonService hoaDonService;
  @Mock private KhuyenMaiService khuyenMaiService;
  @Mock private MauSanPhamService mauSanPhamService;
  @Mock private SizeSanPhamService sizeSanPhamService;
  @Mock private NhanVienService nhanVienService;

  private QuanLyAdminController controller;

  @BeforeEach
  void setUp() {
    controller =
        new QuanLyAdminController(
            servletContext,
            sanPhamService,
            chiTietSanPhamService,
            danhMucSanPhamService,
            hoaDonService,
            khuyenMaiService,
            mauSanPhamService,
            sizeSanPhamService,
            nhanVienService);
  }

  @Test
  void productListRequiresAdminRole() {
    assertEquals(
        "redirect:/admin/dangnhap", controller.Default(new ModelMap(), new MockHttpSession()));
    assertEquals("401", controller.Default(new ModelMap(), sessionWithRole(3)));
    verifyNoInteractions(
        sanPhamService, danhMucSanPhamService, mauSanPhamService, sizeSanPhamService);

    when(sanPhamService.getAllSanPham()).thenReturn(List.of());
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(List.of());
    when(mauSanPhamService.getAllMauSanPham()).thenReturn(List.of());
    when(sizeSanPhamService.getAllSizeSanPham()).thenReturn(List.of());
    ModelMap model = new ModelMap();

    assertEquals("admin/products_admin", controller.Default(model, sessionWithRole(1)));
    assertTrue(model.containsAttribute("listSanPhamss"));
    assertTrue(model.containsAttribute("danhMucSanPhams"));
    assertTrue(model.containsAttribute("mauSanPhams"));
    assertTrue(model.containsAttribute("sizeSanPhams"));
  }

  @Test
  void bulkDeleteReturnsPartialResultsAndPreservesProductsWithHistory() {
    when(sanPhamService.deleteProductIfUnreferenced(1))
        .thenReturn(SanPhamService.ProductDeleteStatus.DELETED);
    when(sanPhamService.deleteProductIfUnreferenced(2))
        .thenReturn(SanPhamService.ProductDeleteStatus.HAS_HISTORY);

    var unauthorized = controller.xoasanpham("1,2", new MockHttpSession());
    assertEquals(401, unauthorized.getStatusCode().value());
    verifyNoInteractions(sanPhamService, chiTietSanPhamService);

    var response = controller.xoasanpham("1,invalid,2", sessionWithRole(1));
    assertEquals(200, response.getStatusCode().value());
    assertEquals(List.of(1), response.getBody().get("deletedIds"));
    assertInstanceOf(Map.class, response.getBody().get("failedIds"));
    Map<?, ?> failedIds = (Map<?, ?>) response.getBody().get("failedIds");
    assertEquals("Mã sản phẩm không hợp lệ.", failedIds.get("invalid"));
    assertTrue(failedIds.get("2").toString().contains("hãy ẩn sản phẩm"));
    verify(sanPhamService).deleteProductIfUnreferenced(1);
    verify(sanPhamService).deleteProductIfUnreferenced(2);
    verifyNoInteractions(chiTietSanPhamService);
  }

  @Test
  void updateProductRequiresAdminAndAddsFoundProductToModel() {
    assertEquals("401", controller.updatesanpham(4, new ModelMap(), new MockHttpSession()));

    SanPham product = new SanPham();
    product.setMasanpham(4);
    when(sanPhamService.getListSanPhamById(4)).thenReturn(product);
    ModelMap model = new ModelMap();

    assertEquals("admin/products_admin", controller.updatesanpham(4, model, sessionWithRole(1)));
    assertSame(product, model.get("sanphamRS"));
  }

  @Test
  void invalidNewProductKeepsInventoryChoicesAndReopensCreationMode() {
    DanhMucSanPham category = new DanhMucSanPham();
    category.setMadanhmucsanpham(1);
    SizeSanPham size = new SizeSanPham();
    size.setMasize(2);
    MauSanPham color = new MauSanPham();
    color.setMamau(3);
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(List.of(category));
    when(sizeSanPhamService.getAllSizeSanPham()).thenReturn(List.of(size));
    when(mauSanPhamService.getAllMauSanPham()).thenReturn(List.of(color));
    ModelMap model = new ModelMap();

    assertEquals(
        "admin/products_admin",
        controller.saveProduct(
            null,
            "",
            1,
            "1000",
            "Description",
            "Adult",
            null,
            null,
            2,
            3,
            7,
            model,
            sessionWithRole(1)));

    assertEquals(false, model.get("editingProduct"));
    assertEquals(2, model.get("selectedSizeId"));
    assertEquals(3, model.get("selectedColorId"));
    assertEquals(7, model.get("initialQuantity"));
    assertEquals("", ((SanPham) model.get("sanphamRS")).getTensanpham());
  }

  @Test
  void numericIdValidationRejectsMalformedInput() {
    assertTrue(QuanLyAdminController.isNumeric("123"));
    assertFalse(QuanLyAdminController.isNumeric("not-a-number"));
    assertFalse(QuanLyAdminController.isNumeric(null));
  }

  @Test
  void newProductPersistsDefaultImageNameWhenNoImageWasUploaded() {
    DanhMucSanPham category = new DanhMucSanPham();
    category.setMadanhmucsanpham(1);
    SizeSanPham size = new SizeSanPham();
    size.setMasize(1);
    MauSanPham color = new MauSanPham();
    color.setMamau(1);
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(List.of(category));
    when(sizeSanPhamService.getAllSizeSanPham()).thenReturn(List.of(size));
    when(mauSanPhamService.getAllMauSanPham()).thenReturn(List.of(color));
    when(sanPhamService.checkNameSanPhamBeforeAdd("Coat")).thenReturn(false);
    when(sanPhamService.addSanPham(any(SanPham.class)))
        .thenAnswer(
            invocation -> {
              ((SanPham) invocation.getArgument(0)).setMasanpham(20);
              return 20;
            });
    when(chiTietSanPhamService.addCTSanPham(any(ChiTietSanPham.class))).thenReturn(30);

    assertEquals(
        "redirect:/admin/product?saved=true",
        controller.saveProduct(
            null,
            "Coat",
            1,
            "1000",
            "",
            "Adult",
            "",
            null,
            1,
            1,
            0,
            new ModelMap(),
            sessionWithRole(1)));

    ArgumentCaptor<SanPham> productCaptor = ArgumentCaptor.forClass(SanPham.class);
    verify(sanPhamService).addSanPham(productCaptor.capture());
    assertEquals("product12.jpg", productCaptor.getValue().getHinhsanpham());
  }

  @Test
  void updatingProductWithoutNewImageKeepsExistingImage() {
    DanhMucSanPham category = new DanhMucSanPham();
    category.setMadanhmucsanpham(1);
    SanPham existingProduct = new SanPham();
    existingProduct.setMasanpham(5);
    existingProduct.setHinhsanpham("existing-image.jpg");
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(List.of(category));
    when(sanPhamService.getListSanPhamById(5)).thenReturn(existingProduct);
    when(sanPhamService.updateSanPham(existingProduct)).thenReturn(true);

    assertEquals(
        "redirect:/admin/product?saved=true",
        controller.saveProduct(
            5,
            "Updated coat",
            1,
            "250000",
            "Updated description",
            "Adult",
            "",
            null,
            null,
            null,
            null,
            new ModelMap(),
            sessionWithRole(1)));
    assertEquals("existing-image.jpg", existingProduct.getHinhsanpham());
    verify(sanPhamService).updateSanPham(existingProduct);
  }

  private MockHttpSession sessionWithRole(int role) {
    MockHttpSession session = new MockHttpSession();
    TaiKhoanLogin account = new TaiKhoanLogin();
    account.setMachucvu(role);
    session.setAttribute("taikhoan", account);
    return session;
  }
}

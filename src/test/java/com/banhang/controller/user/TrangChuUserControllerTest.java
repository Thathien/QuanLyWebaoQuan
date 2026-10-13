package com.banhang.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.SanPham;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.SanPhamService;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit test / Integration test cho Controller TrangChuUserController. Sử dụng MockMvc để kiểm tra
 * định tuyến HTTP, thuộc tính model và view trả về.
 */
@WebMvcTest(TrangChuUserController.class)
public class TrangChuUserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private DanhMucSanPhamService danhMucSanPhamService;

  @MockitoBean private SanPhamService sanPhamService;

  private MockHttpSession session;

  @BeforeEach
  public void setUp() {
    session = new MockHttpSession();
  }

  @Test
  public void testDefault_GuestUser() throws Exception {
    // Cấu hình Mock cho các service được gọi
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(Collections.emptyList());
    when(sanPhamService.getStorefrontProducts(
            eq(null), eq(""), eq(null), eq(null), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));

    // Thực hiện GET request
    mockMvc
        .perform(get("/").session(session))
        .andExpect(status().isOk())
        .andExpect(view().name("index_user"))
        .andExpect(model().attributeExists("showListDanhMucSP"))
        .andExpect(model().attributeExists("showListSanPham"))
        .andExpect(model().attributeExists("productPage"));

    // Xác nhận các service đã được gọi đúng số lần
    verify(danhMucSanPhamService, times(1)).getAllDanhMucSanPham();
    verify(sanPhamService, times(1))
        .getStorefrontProducts(eq(null), eq(""), eq(null), eq(null), eq(PageRequest.of(0, 9)));
  }

  @Test
  public void testStorefrontFiltersArePassedToService() throws Exception {
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(Collections.emptyList());
    when(sanPhamService.getStorefrontProducts(
            eq(2),
            eq("sơ mi"),
            eq(new BigDecimal("100000")),
            eq(new BigDecimal("500000")),
            eq(PageRequest.of(0, 9))))
        .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 9), 0));

    mockMvc
        .perform(
            get("/")
                .param("category", "2")
                .param("q", " sơ mi ")
                .param("minPrice", "100000")
                .param("maxPrice", "500000")
                .session(session))
        .andExpect(status().isOk())
        .andExpect(model().attribute("selectedCategory", 2))
        .andExpect(model().attribute("searchQuery", "sơ mi"))
        .andExpect(model().attribute("minPrice", new BigDecimal("100000")))
        .andExpect(model().attribute("maxPrice", new BigDecimal("500000")));

    verify(sanPhamService)
        .getStorefrontProducts(
            2, "sơ mi", new BigDecimal("100000"), new BigDecimal("500000"), PageRequest.of(0, 9));
  }

  @Test
  public void testProductPaginationUsesRequestedPage() throws Exception {
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(Collections.emptyList());
    when(sanPhamService.getStorefrontProducts(
            eq(null), eq(""), eq(null), eq(null), eq(PageRequest.of(1, 9))))
        .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 9), 20));

    var result =
        mockMvc
            .perform(get("/").param("page", "1").session(session))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("productPage"))
            .andReturn();

    org.springframework.data.domain.Page<?> page =
        (org.springframework.data.domain.Page<?>)
            result.getModelAndView().getModel().get("productPage");
    assertEquals(1, page.getNumber());
    assertEquals(3, page.getTotalPages());
    verify(sanPhamService)
        .getStorefrontProducts(eq(null), eq(""), eq(null), eq(null), eq(PageRequest.of(1, 9)));
  }

  @Test
  public void testDefault_AdminUserRedirect() throws Exception {
    // Giả lập tài khoản đăng nhập là Admin (chức vụ = 1)
    TaiKhoanLogin adminLogin = new TaiKhoanLogin();
    adminLogin.setMachucvu(1);
    session.setAttribute("taikhoan", adminLogin);

    // Thực hiện GET request
    mockMvc
        .perform(get("/").session(session))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin"));

    // Khi redirect sang admin, không được gọi các danh mục/sản phẩm của user homepage
    verifyNoInteractions(danhMucSanPhamService);
    verifyNoInteractions(sanPhamService);
  }

  @Test
  public void testChiTietSanPham() throws Exception {
    int productId = 12;
    SanPham product = new SanPham();
    product.setMasanpham(productId);
    product.setTensanpham("Áo Khoác Da");
    product.setHiden(false);

    when(sanPhamService.getListSanPhamById(productId)).thenReturn(product);
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/chi-tiet-san-pham/{id}", productId))
        .andExpect(status().isOk())
        .andExpect(view().name("product-details_user"))
        .andExpect(model().attribute("sanphamRS", product))
        .andExpect(model().attribute("productImageName", "product12.jpg"))
        .andExpect(model().attributeExists("showListDanhMucSP"));

    verify(sanPhamService, times(1)).getListSanPhamById(productId);
    verify(danhMucSanPhamService, times(1)).getAllDanhMucSanPham();
  }
}

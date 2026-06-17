package com.banhang.controller.user;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.SanPham;
import com.banhang.service.DanhMucSanPhamService;
import com.banhang.service.SanPhamService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
    when(sanPhamService.getAllSanPham()).thenReturn(Collections.emptyList());

    // Thực hiện GET request
    mockMvc
        .perform(get("/").session(session))
        .andExpect(status().isOk())
        .andExpect(view().name("index_user"))
        .andExpect(model().attributeExists("showListDanhMucSP"))
        .andExpect(model().attributeExists("showListSanPham"));

    // Xác nhận các service đã được gọi đúng số lần
    verify(danhMucSanPhamService, times(1)).getAllDanhMucSanPham();
    verify(sanPhamService, times(1)).getAllSanPham();
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

    when(sanPhamService.getListSanPhamById(productId)).thenReturn(product);
    when(danhMucSanPhamService.getAllDanhMucSanPham()).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/chi-tiet-san-pham/{id}", productId))
        .andExpect(status().isOk())
        .andExpect(view().name("product-details_user"))
        .andExpect(model().attribute("sanphamRS", product))
        .andExpect(model().attributeExists("showListDanhMucSP"));

    verify(sanPhamService, times(1)).getListSanPhamById(productId);
    verify(danhMucSanPhamService, times(1)).getAllDanhMucSanPham();
  }
}

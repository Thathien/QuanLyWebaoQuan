package com.banhang.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.banhang.dao.SanPhamDao;
import com.banhang.entity.SanPham;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test cho lớp nghiệp vụ SanPhamService. Sử dụng Mockito để giả lập tầng DAO mà không cần kết
 * nối cơ sở dữ liệu thực.
 */
@ExtendWith(MockitoExtension.class)
public class SanPhamServiceTest {

  @Mock private SanPhamDao sanPhamDao;

  @InjectMocks private SanPhamService sanPhamService;

  private SanPham sampleProduct1;
  private SanPham sampleProduct2;

  @BeforeEach
  public void setUp() {
    sampleProduct1 = new SanPham();
    sampleProduct1.setMasanpham(1);
    sampleProduct1.setTensanpham("Áo Thun Nam");
    sampleProduct1.setGiatien("150000");

    sampleProduct2 = new SanPham();
    sampleProduct2.setMasanpham(2);
    sampleProduct2.setTensanpham("Quần Jeans Nữ");
    sampleProduct2.setGiatien("350000");
  }

  @Test
  public void testGetAllSanPham() {
    // Cấu hình mock
    when(sanPhamDao.getAllSanPham()).thenReturn(Arrays.asList(sampleProduct1, sampleProduct2));

    // Thực thi
    List<SanPham> result = sanPhamService.getAllSanPham();

    // Kiểm chứng
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Áo Thun Nam", result.get(0).getTensanpham());
    verify(sanPhamDao, times(1)).getAllSanPham();
  }

  @Test
  public void testGetListSanPhamById_Found() {
    // Cấu hình mock
    when(sanPhamDao.getListSanPhamById(1)).thenReturn(sampleProduct1);

    // Thực thi
    SanPham result = sanPhamService.getListSanPhamById(1);

    // Kiểm chứng
    assertNotNull(result);
    assertEquals(1, result.getMasanpham());
    assertEquals("Áo Thun Nam", result.getTensanpham());
    verify(sanPhamDao, times(1)).getListSanPhamById(1);
  }

  @Test
  public void testGetListSanPhamById_NotFound() {
    // Cấu hình mock
    when(sanPhamDao.getListSanPhamById(999)).thenReturn(null);

    // Thực thi
    SanPham result = sanPhamService.getListSanPhamById(999);

    // Kiểm chứng
    assertNull(result);
    verify(sanPhamDao, times(1)).getListSanPhamById(999);
  }

  @Test
  public void testAddSanPham() {
    // Cấu hình mock
    when(sanPhamDao.addSanPham(sampleProduct1)).thenReturn(1);

    // Thực thi
    int newId = sanPhamService.addSanPham(sampleProduct1);

    // Kiểm chứng
    assertEquals(1, newId);
    verify(sanPhamDao, times(1)).addSanPham(sampleProduct1);
  }
}

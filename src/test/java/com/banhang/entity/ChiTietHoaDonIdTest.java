package com.banhang.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit test cho lớp thực thể / POJO ChiTietHoaDonId. Chứng minh cách viết unit test cho các lớp
 * Java thuần thông thường.
 */
public class ChiTietHoaDonIdTest {

  @Test
  public void testEqualsAndHashCode() {
    ChiTietHoaDonId id1 = new ChiTietHoaDonId();
    id1.setMahoadon(1);
    id1.setMachitietsanpham(100);

    ChiTietHoaDonId id2 = new ChiTietHoaDonId();
    id2.setMahoadon(1);
    id2.setMachitietsanpham(100);

    ChiTietHoaDonId id3 = new ChiTietHoaDonId();
    id3.setMahoadon(2);
    id3.setMachitietsanpham(100);

    // Kiểm tra tính bằng nhau (equals)
    assertEquals(id1, id2, "Hai đối tượng có cùng thuộc tính phải equals bằng true");
    assertNotEquals(id1, id3, "Hai đối tượng có mã hóa đơn khác nhau phải equals bằng false");

    // Kiểm tra mã băm (hashCode)
    assertEquals(id1.hashCode(), id2.hashCode(), "Hai đối tượng bằng nhau phải có cùng hashCode");
    assertNotEquals(
        id1.hashCode(), id3.hashCode(), "Hai đối tượng khác nhau nên có hashCode khác nhau");
  }

  @Test
  public void testGettersAndSetters() {
    ChiTietHoaDonId id = new ChiTietHoaDonId();
    id.setMahoadon(5);
    id.setMachitietsanpham(25);

    assertEquals(5, id.getMahoadon());
    assertEquals(25, id.getMachitietsanpham());
  }
}

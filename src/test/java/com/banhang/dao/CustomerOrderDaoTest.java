package com.banhang.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.ChiTietYeuCauDoiTra;
import com.banhang.entity.HoaDon;
import com.banhang.entity.LoaiYeuCauDoiTra;
import com.banhang.entity.MauSanPham;
import com.banhang.entity.SanPham;
import com.banhang.entity.SizeSanPham;
import com.banhang.entity.YeuCauDoiTra;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:customer-orders;MODE=MSSQLServer;DB_CLOSE_DELAY=-1",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.show-sql=false",
      "spring.sql.init.mode=never"
    })
@Import({HoaDonDao.class, YeuCauDoiTraDao.class})
class CustomerOrderDaoTest {
  @Autowired private EntityManager entityManager;
  @Autowired private HoaDonDao hoaDonDao;
  @Autowired private YeuCauDoiTraDao yeuCauDoiTraDao;

  @Test
  void loadsOnlyCustomerOrderDetailsAndReturnRequestLines() {
    HoaDon order = new HoaDon();
    order.setEmailkhachhang("customer@example.com");
    order.setTinhtrang(3);
    entityManager.persist(order);

    SanPham product = new SanPham();
    product.setTensanpham("Áo");
    product.setHiden(false);
    entityManager.persist(product);
    SizeSanPham size = new SizeSanPham();
    size.setSize("M");
    entityManager.persist(size);
    MauSanPham color = new MauSanPham();
    color.setTenmau("Đỏ");
    entityManager.persist(color);

    ChiTietSanPham variant = new ChiTietSanPham();
    variant.setSanPham(product);
    variant.setSizeSanPham(size);
    variant.setMauSanPham(color);
    variant.setSoluong(4);
    entityManager.persist(variant);

    ChiTietHoaDonId orderLineId = new ChiTietHoaDonId();
    orderLineId.setMahoadon(order.getMahoadon());
    orderLineId.setMachitietsanpham(variant.getMachitietsanpham());
    ChiTietHoaDon orderLine = new ChiTietHoaDon();
    orderLine.setChiTietHoaDonId(orderLineId);
    orderLine.setGiatien("1000");
    orderLine.setSoluong(2);
    entityManager.persist(orderLine);

    YeuCauDoiTra request = new YeuCauDoiTra();
    request.setHoaDon(order);
    request.setLoaiYeuCau(LoaiYeuCauDoiTra.TRA);
    request.setLyDo("Không vừa");
    entityManager.persist(request);
    ChiTietYeuCauDoiTra requestLine = new ChiTietYeuCauDoiTra();
    requestLine.setYeuCau(request);
    requestLine.setBienThe(variant);
    requestLine.setSoLuong(1);
    entityManager.persist(requestLine);
    entityManager.flush();
    entityManager.clear();

    HoaDon customerOrder =
        hoaDonDao.getCustomerOrderById(order.getMahoadon(), "CUSTOMER@example.com");
    assertNotNull(customerOrder);
    assertEquals(
        "Áo",
        customerOrder
            .getChiTietHoaDon()
            .iterator()
            .next()
            .getBienThe()
            .getSanPham()
            .getTensanpham());
    assertTrue(hoaDonDao.getCustomerOrderById(order.getMahoadon(), "another@example.com") == null);
    assertEquals(
        1, yeuCauDoiTraDao.requestedQuantity(order.getMahoadon(), variant.getMachitietsanpham()));
    List<YeuCauDoiTra> requests = yeuCauDoiTraDao.findByOrder(order.getMahoadon());
    assertEquals(1, requests.size());
    assertEquals(
        "Đỏ", requests.getFirst().getChiTiet().getFirst().getBienThe().getMauSanPham().getTenmau());
    assertEquals(1, yeuCauDoiTraDao.findAll().size());
  }
}

package com.banhang.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banhang.dao.KhoDao;
import com.banhang.dao.YeuCauDoiTraDao;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.HoaDon;
import com.banhang.entity.LoaiYeuCauDoiTra;
import com.banhang.entity.SanPham;
import com.banhang.entity.TrangThaiYeuCauDoiTra;
import com.banhang.entity.YeuCauDoiTra;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class YeuCauDoiTraServiceTest {
  @Mock private KhoDao khoDao;
  @Mock private YeuCauDoiTraDao yeuCauDoiTraDao;
  @InjectMocks private YeuCauDoiTraService service;

  @Test
  void createsReturnRequestForOwnedCompletedOrderWithinThirtyDays() {
    HoaDon order = completedOrder();
    ChiTietHoaDon orderLine = new ChiTietHoaDon();
    orderLine.setSoluong(3);
    ChiTietSanPham original = variant(20, 8, 10);
    when(khoDao.findOrderForUpdate(10)).thenReturn(order);
    when(khoDao.getOrderLine(10, 20)).thenReturn(orderLine);
    when(khoDao.returnedQuantity(10, 20)).thenReturn(0L);
    when(khoDao.refundedQuantity(10, 20)).thenReturn(0L);
    when(yeuCauDoiTraDao.requestedQuantity(10, 20)).thenReturn(0L);
    when(khoDao.findVariantForUpdate(20)).thenReturn(original);

    YeuCauDoiTra request =
        service.createRequest(
            10,
            "customer@example.com",
            LoaiYeuCauDoiTra.TRA,
            "Không vừa",
            List.of(new YeuCauDoiTraService.RequestLine(20, 2, null)));

    assertEquals(TrangThaiYeuCauDoiTra.CHO_XU_LY, request.getTrangThai());
    assertEquals(1, request.getChiTiet().size());
    assertEquals(2, request.getChiTiet().getFirst().getSoLuong());
    verify(yeuCauDoiTraDao).persist(request);
    verify(yeuCauDoiTraDao).persist(request.getChiTiet().getFirst());
    verify(yeuCauDoiTraDao).flush();
  }

  @Test
  void rejectsReturnRequestAfterThirtyDays() {
    HoaDon order = completedOrder();
    order.setCompletedAt(LocalDateTime.now().minusDays(31));
    when(khoDao.findOrderForUpdate(10)).thenReturn(order);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            service.createRequest(
                10,
                "customer@example.com",
                LoaiYeuCauDoiTra.TRA,
                "Không vừa",
                List.of(new YeuCauDoiTraService.RequestLine(20, 1, null))));
    verify(yeuCauDoiTraDao, never()).persist(any());
  }

  @Test
  void exchangeMustUseAnotherVariantOfTheSameProductWithEnoughStock() {
    HoaDon order = completedOrder();
    ChiTietHoaDon orderLine = new ChiTietHoaDon();
    orderLine.setSoluong(3);
    ChiTietSanPham original = variant(20, 8, 10);
    ChiTietSanPham replacement = variant(21, 9, 10);
    when(khoDao.findOrderForUpdate(10)).thenReturn(order);
    when(khoDao.getOrderLine(10, 20)).thenReturn(orderLine);
    when(khoDao.returnedQuantity(10, 20)).thenReturn(0L);
    when(khoDao.refundedQuantity(10, 20)).thenReturn(0L);
    when(yeuCauDoiTraDao.requestedQuantity(10, 20)).thenReturn(0L);
    when(khoDao.findVariantForUpdate(20)).thenReturn(original);
    when(khoDao.findVariantForUpdate(21)).thenReturn(replacement);

    YeuCauDoiTra request =
        service.createRequest(
            10,
            "customer@example.com",
            LoaiYeuCauDoiTra.DOI,
            "Đổi size",
            List.of(new YeuCauDoiTraService.RequestLine(20, 2, 21)));

    assertEquals(21, request.getChiTiet().getFirst().getBienTheThayThe().getMachitietsanpham());
  }

  @Test
  void exchangeChecksCombinedDemandForRepeatedReplacementVariant() {
    when(khoDao.findOrderForUpdate(10)).thenReturn(completedOrder());
    when(khoDao.getOrderLine(10, 20)).thenReturn(orderLine(2));
    when(khoDao.getOrderLine(10, 22)).thenReturn(orderLine(2));
    when(khoDao.returnedQuantity(10, 20)).thenReturn(0L);
    when(khoDao.refundedQuantity(10, 20)).thenReturn(0L);
    when(yeuCauDoiTraDao.requestedQuantity(10, 20)).thenReturn(0L);
    when(khoDao.returnedQuantity(10, 22)).thenReturn(0L);
    when(khoDao.refundedQuantity(10, 22)).thenReturn(0L);
    when(yeuCauDoiTraDao.requestedQuantity(10, 22)).thenReturn(0L);
    when(khoDao.findVariantForUpdate(20)).thenReturn(variant(20, 4, 10));
    when(khoDao.findVariantForUpdate(22)).thenReturn(variant(22, 4, 10));
    when(khoDao.findVariantForUpdate(21)).thenReturn(variant(21, 3, 10));

    assertThrows(
        IllegalArgumentException.class,
        () ->
            service.createRequest(
                10,
                "customer@example.com",
                LoaiYeuCauDoiTra.DOI,
                "Đổi size",
                List.of(
                    new YeuCauDoiTraService.RequestLine(20, 2, 21),
                    new YeuCauDoiTraService.RequestLine(22, 2, 21))));
    verify(yeuCauDoiTraDao, never()).flush();
  }

  @Test
  void rejectsOrderOwnedByAnotherCustomer() {
    HoaDon order = completedOrder();
    order.setEmailkhachhang("someone-else@example.com");
    when(khoDao.findOrderForUpdate(10)).thenReturn(order);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            service.createRequest(
                10,
                "customer@example.com",
                LoaiYeuCauDoiTra.TRA,
                "Không vừa",
                List.of(new YeuCauDoiTraService.RequestLine(20, 1, null))));
  }

  @Test
  void onlyAllowsPendingRequestDecision() {
    YeuCauDoiTra request = new YeuCauDoiTra();
    request.setTrangThai(TrangThaiYeuCauDoiTra.CHO_XU_LY);
    when(yeuCauDoiTraDao.findForUpdate(5)).thenReturn(request);

    service.updateRequestStatus(5, TrangThaiYeuCauDoiTra.CHAP_NHAN);

    assertEquals(TrangThaiYeuCauDoiTra.CHAP_NHAN, request.getTrangThai());
    verify(yeuCauDoiTraDao).findForUpdate(5);
  }

  private HoaDon completedOrder() {
    HoaDon order = new HoaDon();
    order.setTinhtrang(3);
    order.setEmailkhachhang("customer@example.com");
    order.setCompletedAt(LocalDateTime.now().minusDays(10));
    return order;
  }

  private ChiTietSanPham variant(int id, int stock, int productId) {
    ChiTietSanPham variant = new ChiTietSanPham();
    variant.setMachitietsanpham(id);
    variant.setSoluong(stock);
    SanPham product = new SanPham();
    product.setMasanpham(productId);
    product.setHiden(false);
    variant.setSanPham(product);
    return variant;
  }

  private ChiTietHoaDon orderLine(int quantity) {
    ChiTietHoaDon line = new ChiTietHoaDon();
    line.setSoluong(quantity);
    return line;
  }
}

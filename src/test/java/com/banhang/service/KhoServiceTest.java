package com.banhang.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banhang.dao.KhoDao;
import com.banhang.dto.report.RefundRequest;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.HoaDon;
import com.banhang.entity.HoanTien;
import com.banhang.entity.LoaiBienDongKho;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KhoServiceTest {
  @Mock private KhoDao khoDao;
  @InjectMocks private KhoService khoService;

  @Test
  void refundAmountCannotExceedValueOfReturnedQuantity() {
    HoaDon order = new HoaDon();
    order.setTinhtrang(3);
    ChiTietHoaDon orderLine = new ChiTietHoaDon();
    orderLine.setSoluong(5);
    orderLine.setGiatien("100");
    when(khoDao.findOrderForUpdate(21)).thenReturn(order);
    when(khoDao.getOrderLine(21, 7)).thenReturn(orderLine);
    when(khoDao.refundedQuantity(21, 7)).thenReturn(0L);
    when(khoDao.refundedAmount(21, 7)).thenReturn(BigDecimal.ZERO);

    RefundRequest request =
        new RefundRequest(
            "Hoàn một phần", List.of(new RefundRequest.RefundLine(7, BigDecimal.valueOf(250), 2)));

    assertThrows(IllegalArgumentException.class, () -> khoService.refundOrder(21, request));
    verify(khoDao, never()).persist(any(HoanTien.class));
  }

  @Test
  void refundAmountMayNotExceedRemainingAmountOfOrderLine() {
    HoaDon order = new HoaDon();
    order.setTinhtrang(3);
    ChiTietHoaDon orderLine = new ChiTietHoaDon();
    orderLine.setSoluong(5);
    orderLine.setGiatien("100");
    when(khoDao.findOrderForUpdate(21)).thenReturn(order);
    when(khoDao.getOrderLine(21, 7)).thenReturn(orderLine);
    when(khoDao.refundedQuantity(21, 7)).thenReturn(1L);
    when(khoDao.refundedAmount(21, 7)).thenReturn(BigDecimal.valueOf(100));

    RefundRequest request =
        new RefundRequest(
            "Hoàn một phần", List.of(new RefundRequest.RefundLine(7, BigDecimal.valueOf(401), 4)));

    assertThrows(IllegalArgumentException.class, () -> khoService.refundOrder(21, request));
    verify(khoDao, never()).persist(any(HoanTien.class));
  }

  @Test
  void customerCanCancelOnlyTheirOwnOrderWhileItIsAwaitingConfirmation() {
    HoaDon order = new HoaDon();
    order.setTinhtrang(0);
    order.setEmailkhachhang("customer@example.com");
    ChiTietHoaDon line = new ChiTietHoaDon();
    ChiTietHoaDonId lineId = new ChiTietHoaDonId();
    lineId.setMachitietsanpham(7);
    line.setChiTietHoaDonId(lineId);
    line.setSoluong(2);
    when(khoDao.findOrderForUpdate(21)).thenReturn(order);
    when(khoDao.getOrderLines(21)).thenReturn(List.of(line));
    ChiTietSanPham variant = new ChiTietSanPham();
    variant.setSoluong(4);
    when(khoDao.findVariantForUpdate(7)).thenReturn(variant);

    khoService.cancelCustomerOrder(21, "CUSTOMER@example.com");

    assertEquals(6, variant.getSoluong());
    assertEquals(4, order.getTinhtrang());
    verify(khoDao)
        .saveMovement(
            variant,
            LoaiBienDongKho.HUY_DON.getCode(),
            2,
            null,
            "ORDER:21",
            "Hoàn tồn kho do hủy đơn");
  }

  @Test
  void customerCannotCancelSomeoneElsesOrAlreadyApprovedOrder() {
    HoaDon someoneElsesOrder = new HoaDon();
    someoneElsesOrder.setTinhtrang(0);
    someoneElsesOrder.setEmailkhachhang("other@example.com");
    when(khoDao.findOrderForUpdate(21)).thenReturn(someoneElsesOrder);
    assertThrows(
        IllegalArgumentException.class,
        () -> khoService.cancelCustomerOrder(21, "customer@example.com"));

    HoaDon approvedOrder = new HoaDon();
    approvedOrder.setTinhtrang(1);
    approvedOrder.setEmailkhachhang("customer@example.com");
    when(khoDao.findOrderForUpdate(22)).thenReturn(approvedOrder);
    assertThrows(
        IllegalArgumentException.class,
        () -> khoService.cancelCustomerOrder(22, "customer@example.com"));
    verify(khoDao, never()).getOrderLines(21);
    verify(khoDao, never()).getOrderLines(22);
  }
}

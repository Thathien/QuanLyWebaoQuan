package com.banhang.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banhang.dao.KhoDao;
import com.banhang.dto.report.RefundRequest;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.HoaDon;
import com.banhang.entity.HoanTien;
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
}

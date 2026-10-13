package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.dto.report.RefundRequest;
import com.banhang.dto.report.ReturnRequest;
import com.banhang.service.KhoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderAccountingAdminController {
  private final KhoService khoService;

  public OrderAccountingAdminController(KhoService khoService) {
    this.khoService = khoService;
  }

  @PostMapping("/{id}/cancel")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelOrder(@PathVariable int id, HttpSession session) {
    requireAdmin(session);
    khoService.cancelOrder(id);
  }

  @PostMapping("/{id}/refunds")
  @ResponseStatus(HttpStatus.CREATED)
  public void refund(
      @PathVariable int id, @Valid @RequestBody RefundRequest request, HttpSession session) {
    requireAdmin(session);
    khoService.refundOrder(id, request);
  }

  @PostMapping("/{id}/returns")
  @ResponseStatus(HttpStatus.CREATED)
  public void receiveReturn(
      @PathVariable int id, @Valid @RequestBody ReturnRequest request, HttpSession session) {
    requireAdmin(session);
    khoService.receiveReturn(id, request);
  }

  private void requireAdmin(HttpSession session) {
    Object accountValue = session.getAttribute("taikhoan");
    if (!(accountValue instanceof TaiKhoanLogin account)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập.");
    }
    if (account.getMachucvu() != 1) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ quản trị viên được thao tác.");
    }
  }
}

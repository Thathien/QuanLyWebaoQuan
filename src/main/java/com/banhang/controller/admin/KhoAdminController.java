package com.banhang.controller.admin;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.dto.report.InventoryAdjustmentRequest;
import com.banhang.dto.report.InventoryVariantResponse;
import com.banhang.dto.report.NhaCungCapRequest;
import com.banhang.dto.report.NhaCungCapResponse;
import com.banhang.dto.report.OpeningBalanceRequest;
import com.banhang.dto.report.PhieuNhapRequest;
import com.banhang.dto.report.PhieuNhapResponse;
import com.banhang.dto.report.ReceiptAdminResponse;
import com.banhang.service.KhoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/inventory")
public class KhoAdminController {
  private final KhoService khoService;

  public KhoAdminController(KhoService khoService) {
    this.khoService = khoService;
  }

  @GetMapping("/suppliers")
  public List<NhaCungCapResponse> suppliers(HttpSession session) {
    requireAdmin(session);
    return khoService.getSuppliers().stream()
        .map(
            supplier ->
                new NhaCungCapResponse(
                    supplier.getId(),
                    supplier.getTen(),
                    supplier.getEmail(),
                    supplier.getSoDienThoai(),
                    supplier.getDiaChi()))
        .toList();
  }

  @GetMapping("/variants")
  public List<InventoryVariantResponse> variants(HttpSession session) {
    requireAdmin(session);
    return khoService.getVariants();
  }

  @GetMapping("/receipts")
  public List<ReceiptAdminResponse> receipts(HttpSession session) {
    requireAdmin(session);
    return khoService.getReceipts();
  }

  @PostMapping("/suppliers")
  @ResponseStatus(HttpStatus.CREATED)
  public NhaCungCapResponse createSupplier(
      @Valid @RequestBody NhaCungCapRequest request, HttpSession session) {
    requireAdmin(session);
    var supplier = khoService.createSupplier(request);
    return new NhaCungCapResponse(
        supplier.getId(),
        supplier.getTen(),
        supplier.getEmail(),
        supplier.getSoDienThoai(),
        supplier.getDiaChi());
  }

  @PostMapping("/receipts")
  @ResponseStatus(HttpStatus.CREATED)
  public PhieuNhapResponse createReceipt(
      @Valid @RequestBody PhieuNhapRequest request, HttpSession session) {
    requireAdmin(session);
    var receipt = khoService.createDraftReceipt(request);
    return new PhieuNhapResponse(
        receipt.getId(), receipt.getNhaCungCap().getId(), receipt.getStatus(), null);
  }

  @PostMapping("/receipts/{id}/confirm")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void confirmReceipt(@PathVariable long id, HttpSession session) {
    requireAdmin(session);
    khoService.confirmReceipt(id);
  }

  @DeleteMapping("/receipts/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelReceipt(@PathVariable long id, HttpSession session) {
    requireAdmin(session);
    khoService.cancelDraftReceipt(id);
  }

  @PostMapping("/opening-balances")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setOpeningBalances(
      @Valid @RequestBody OpeningBalanceRequest request, HttpSession session) {
    requireAdmin(session);
    khoService.setOpeningBalances(request);
  }

  @PostMapping("/adjustments")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void adjustInventory(
      @Valid @RequestBody InventoryAdjustmentRequest request, HttpSession session) {
    requireAdmin(session);
    khoService.adjustInventory(request);
  }

  private void requireAdmin(HttpSession session) {
    Object accountValue = session.getAttribute("taikhoan");
    if (!(accountValue instanceof TaiKhoanLogin account)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập.");
    }
    if (account.getMachucvu() != 1) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Chỉ quản trị viên được thao tác kho.");
    }
  }
}

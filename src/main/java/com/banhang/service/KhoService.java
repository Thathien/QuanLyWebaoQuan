package com.banhang.service;

import com.banhang.dao.KhoDao;
import com.banhang.dto.report.InventoryAdjustmentRequest;
import com.banhang.dto.report.InventoryVariantResponse;
import com.banhang.dto.report.NhaCungCapRequest;
import com.banhang.dto.report.OpeningBalanceRequest;
import com.banhang.dto.report.PhieuNhapRequest;
import com.banhang.dto.report.ReceiptAdminResponse;
import com.banhang.dto.report.RefundRequest;
import com.banhang.dto.report.ReturnRequest;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoanTien;
import com.banhang.entity.ChiTietPhieuNhap;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.ChiTietTraHang;
import com.banhang.entity.HoaDon;
import com.banhang.entity.HoanTien;
import com.banhang.entity.LoaiBienDongKho;
import com.banhang.entity.NhaCungCap;
import com.banhang.entity.PhieuNhap;
import com.banhang.entity.TraHang;
import com.banhang.entity.TrangThaiHoaDon;
import com.banhang.entity.TrangThaiPhieuNhap;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KhoService {
  private final KhoDao khoDao;

  public KhoService(KhoDao khoDao) {
    this.khoDao = khoDao;
  }

  public NhaCungCap createSupplier(NhaCungCapRequest request) {
    NhaCungCap supplier = new NhaCungCap();
    supplier.setTen(request.ten().trim());
    supplier.setEmail(trim(request.email()));
    supplier.setSoDienThoai(trim(request.soDienThoai()));
    supplier.setDiaChi(trim(request.diaChi()));
    return khoDao.persist(supplier);
  }

  @Transactional(readOnly = true)
  public List<NhaCungCap> getSuppliers() {
    return khoDao.getSuppliers();
  }

  @Transactional(readOnly = true)
  public List<InventoryVariantResponse> getVariants() {
    Set<Integer> variantsWithMovements = khoDao.getVariantIdsWithMovements();
    return khoDao.getVariants().stream()
        .map(
            variant ->
                new InventoryVariantResponse(
                    variant.getMachitietsanpham(),
                    variant.getSanPham().getTensanpham(),
                    variant.getSizeSanPham().getSize(),
                    variant.getMauSanPham().getTenmau(),
                    variant.getSoluong(),
                    variant.getGiaVonBinhQuan(),
                    !variantsWithMovements.contains(variant.getMachitietsanpham())))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ReceiptAdminResponse> getReceipts() {
    return khoDao.getReceipts().stream()
        .map(
            receipt ->
                new ReceiptAdminResponse(
                    receipt.getId(),
                    receipt.getNhaCungCap().getId(),
                    receipt.getNhaCungCap().getTen(),
                    receipt.getStatus(),
                    receipt.getCreatedAt(),
                    receipt.getConfirmedAt(),
                    khoDao.getReceiptLines(receipt.getId()).stream()
                        .map(
                            line ->
                                new ReceiptAdminResponse.ReceiptLine(
                                    line.getBienThe().getMachitietsanpham(),
                                    line.getBienThe().getSanPham().getTensanpham(),
                                    line.getBienThe().getSizeSanPham().getSize(),
                                    line.getBienThe().getMauSanPham().getTenmau(),
                                    line.getSoLuong(),
                                    line.getDonGiaVon()))
                        .toList()))
        .toList();
  }

  public PhieuNhap createDraftReceipt(PhieuNhapRequest request) {
    NhaCungCap supplier = khoDao.findSupplier(request.supplierId());
    if (supplier == null) {
      throw new IllegalArgumentException("Không tìm thấy nhà cung cấp.");
    }
    Set<Integer> variantIds = new HashSet<>();
    PhieuNhap receipt = new PhieuNhap();
    receipt.setNhaCungCap(supplier);
    khoDao.persist(receipt);
    for (PhieuNhapRequest.ReceiptLine input : request.items()) {
      if (!variantIds.add(input.variantId())) {
        throw new IllegalArgumentException(
            "Mỗi biến thể chỉ được xuất hiện một lần trong phiếu nhập.");
      }
      ChiTietSanPham variant = khoDao.findVariantForUpdate(input.variantId());
      if (variant == null) {
        throw new IllegalArgumentException("Không tìm thấy biến thể sản phẩm " + input.variantId());
      }
      ChiTietPhieuNhap line = new ChiTietPhieuNhap();
      line.setPhieuNhap(receipt);
      line.setBienThe(variant);
      line.setSoLuong(input.quantity());
      line.setDonGiaVon(money(input.unitCost()));
      khoDao.persist(line);
    }
    khoDao.flush();
    return receipt;
  }

  public void confirmReceipt(long receiptId) {
    PhieuNhap receipt = khoDao.findReceiptForUpdate(receiptId);
    if (receipt == null || !TrangThaiPhieuNhap.NHAP.getCode().equals(receipt.getStatus())) {
      throw new IllegalArgumentException(
          "Phiếu nhập không tồn tại hoặc không còn ở trạng thái nháp.");
    }
    List<ChiTietPhieuNhap> lines = khoDao.getReceiptLines(receiptId);
    if (lines.isEmpty()) {
      throw new IllegalArgumentException("Phiếu nhập không có dòng hàng.");
    }
    for (ChiTietPhieuNhap line : lines) {
      ChiTietSanPham variant = khoDao.findVariantForUpdate(line.getBienThe().getMachitietsanpham());
      updateWeightedCost(variant, line.getSoLuong(), line.getDonGiaVon());
      variant.setSoluong(Math.addExact(variant.getSoluong(), line.getSoLuong()));
      variant.setNgaynhap(LocalDate.now().toString());
      khoDao.merge(variant);
      khoDao.saveMovement(
          variant,
          LoaiBienDongKho.NHAP_HANG.getCode(),
          line.getSoLuong(),
          line.getDonGiaVon(),
          "RECEIPT:" + receiptId,
          "Xác nhận phiếu nhập");
    }
    receipt.setStatus(TrangThaiPhieuNhap.DA_XAC_NHAN.getCode());
    receipt.setConfirmedAt(LocalDateTime.now());
    khoDao.merge(receipt);
  }

  public void cancelDraftReceipt(long receiptId) {
    PhieuNhap receipt = khoDao.findReceiptForUpdate(receiptId);
    if (receipt == null || !TrangThaiPhieuNhap.NHAP.getCode().equals(receipt.getStatus())) {
      throw new IllegalArgumentException("Chỉ có thể hủy phiếu nhập đang ở trạng thái nháp.");
    }
    receipt.setStatus(TrangThaiPhieuNhap.DA_HUY.getCode());
    khoDao.merge(receipt);
  }

  public void setOpeningBalances(OpeningBalanceRequest request) {
    Set<Integer> ids = new HashSet<>();
    for (OpeningBalanceRequest.OpeningLine input : request.items()) {
      if (!ids.add(input.variantId())) {
        throw new IllegalArgumentException("Biến thể bị lặp trong dữ liệu tồn đầu kỳ.");
      }
      ChiTietSanPham variant = khoDao.findVariantForUpdate(input.variantId());
      if (variant == null) {
        throw new IllegalArgumentException("Không tìm thấy biến thể sản phẩm " + input.variantId());
      }
      if (khoDao.hasMovement(input.variantId())) {
        throw new IllegalArgumentException(
            "Biến thể đã có lịch sử kho; hãy điều chỉnh kho thay vì ghi đè tồn đầu kỳ.");
      }
      int delta = input.quantity() - variant.getSoluong();
      variant.setSoluong(input.quantity());
      variant.setGiaVonBinhQuan(money(input.unitCost()));
      variant.setNgaynhap(LocalDate.now().toString());
      khoDao.merge(variant);
      khoDao.saveMovement(
          variant,
          LoaiBienDongKho.TON_DAU_KY.getCode(),
          delta,
          money(input.unitCost()),
          "OPENING:" + input.variantId(),
          "Thiết lập tồn đầu kỳ");
    }
  }

  public void adjustInventory(InventoryAdjustmentRequest request) {
    if (request.quantityDelta() == 0) {
      throw new IllegalArgumentException("Số lượng điều chỉnh không được bằng 0.");
    }
    ChiTietSanPham variant = khoDao.findVariantForUpdate(request.variantId());
    if (variant == null) {
      throw new IllegalArgumentException("Không tìm thấy biến thể sản phẩm.");
    }
    int updated = Math.addExact(variant.getSoluong(), request.quantityDelta());
    if (updated < 0) {
      throw new IllegalArgumentException("Điều chỉnh làm tồn kho âm.");
    }
    if (request.quantityDelta() > 0) {
      if (request.unitCost() == null || request.unitCost().signum() < 0) {
        throw new IllegalArgumentException("Điều chỉnh tăng kho cần đơn giá vốn hợp lệ.");
      }
      updateWeightedCost(variant, request.quantityDelta(), money(request.unitCost()));
    }
    variant.setSoluong(updated);
    khoDao.merge(variant);
    khoDao.saveMovement(
        variant,
        LoaiBienDongKho.DIEU_CHINH.getCode(),
        request.quantityDelta(),
        request.unitCost() == null ? null : money(request.unitCost()),
        null,
        request.reason().trim());
  }

  public void recordSale(ChiTietSanPham variant, int quantity, int orderId) {
    khoDao.saveMovement(
        variant,
        LoaiBienDongKho.BAN_HANG.getCode(),
        -quantity,
        variant.getGiaVonBinhQuan(),
        "ORDER:" + orderId,
        "Trừ kho khi tạo đơn hàng");
  }

  public void cancelOrder(int orderId) {
    HoaDon order = khoDao.findOrderForUpdate(orderId);
    if (order == null
        || (order.getTinhtrang() != TrangThaiHoaDon.CHO_XU_LY.getCode()
            && order.getTinhtrang() != TrangThaiHoaDon.DA_DUYET.getCode())) {
      throw new IllegalArgumentException("Chỉ có thể hủy đơn đang chờ xử lý hoặc đã duyệt.");
    }
    for (ChiTietHoaDon line : khoDao.getOrderLines(orderId)) {
      ChiTietSanPham variant =
          khoDao.findVariantForUpdate(line.getChiTietHoaDonId().getMachitietsanpham());
      variant.setSoluong(Math.addExact(variant.getSoluong(), line.getSoluong()));
      khoDao.merge(variant);
      khoDao.saveMovement(
          variant,
          LoaiBienDongKho.HUY_DON.getCode(),
          line.getSoluong(),
          line.getGiaVon(),
          "ORDER:" + orderId,
          "Hoàn tồn kho do hủy đơn");
    }
    order.setTinhtrang(TrangThaiHoaDon.DA_HUY.getCode());
    khoDao.merge(order);
  }

  public void refundOrder(int orderId, RefundRequest request) {
    HoaDon order = khoDao.findOrderForUpdate(orderId);
    if (order == null || order.getTinhtrang() != TrangThaiHoaDon.HOAN_THANH.getCode()) {
      throw new IllegalArgumentException("Chỉ hoàn tiền cho đơn đã hoàn thành.");
    }
    Set<Integer> ids = new HashSet<>();
    for (RefundRequest.RefundLine input : request.items()) {
      if (!ids.add(input.variantId())) {
        throw new IllegalArgumentException(
            "Mỗi dòng hàng chỉ được xuất hiện một lần trong giao dịch.");
      }
      ChiTietHoaDon line = khoDao.getOrderLine(orderId, input.variantId());
      if (line == null) {
        throw new IllegalArgumentException("Biến thể không thuộc hóa đơn này.");
      }
      long availableQuantity =
          line.getSoluong() - khoDao.refundedQuantity(orderId, input.variantId());
      BigDecimal lineTotal = unitPrice(line).multiply(BigDecimal.valueOf(line.getSoluong()));
      BigDecimal availableAmount =
          lineTotal.subtract(khoDao.refundedAmount(orderId, input.variantId()));
      BigDecimal requestedAmount = money(input.amount());
      BigDecimal requestedQuantityValue =
          unitPrice(line).multiply(BigDecimal.valueOf(input.quantity()));
      if (input.quantity() > availableQuantity
          || requestedAmount.compareTo(availableAmount) > 0
          || requestedAmount.compareTo(requestedQuantityValue) > 0) {
        throw new IllegalArgumentException(
            "Số lượng hoặc tiền hoàn vượt quá giá trị còn lại của dòng hàng.");
      }
    }
    HoanTien refund = new HoanTien();
    refund.setHoaDon(order);
    refund.setLyDo(request.reason().trim());
    khoDao.persist(refund);
    for (RefundRequest.RefundLine input : request.items()) {
      ChiTietHoanTien refundLine = new ChiTietHoanTien();
      refundLine.setHoanTien(refund);
      refundLine.setMaHoaDon(orderId);
      refundLine.setMaBienThe(input.variantId());
      refundLine.setSoLuong(input.quantity());
      refundLine.setSoTien(money(input.amount()));
      khoDao.persist(refundLine);
    }
  }

  public void receiveReturn(int orderId, ReturnRequest request) {
    HoaDon order = khoDao.findOrderForUpdate(orderId);
    if (order == null || order.getTinhtrang() != TrangThaiHoaDon.HOAN_THANH.getCode()) {
      throw new IllegalArgumentException("Chỉ nhận hàng trả về cho đơn đã hoàn thành.");
    }
    TraHang returned = new TraHang();
    returned.setHoaDon(order);
    returned.setLyDo(request.reason().trim());
    khoDao.persist(returned);
    Set<Integer> ids = new HashSet<>();
    for (ReturnRequest.ReturnLine input : request.items()) {
      if (!ids.add(input.variantId())) {
        throw new IllegalArgumentException("Mỗi dòng hàng chỉ được xuất hiện một lần.");
      }
      ChiTietHoaDon line = khoDao.getOrderLine(orderId, input.variantId());
      if (line == null
          || input.quantity()
              > line.getSoluong() - khoDao.returnedQuantity(orderId, input.variantId())) {
        throw new IllegalArgumentException("Số lượng trả vượt quá số lượng còn có thể nhận.");
      }
      ChiTietSanPham variant = khoDao.findVariantForUpdate(input.variantId());
      applyCostedReceipt(variant, input.quantity(), line.getGiaVon());
      variant.setSoluong(Math.addExact(variant.getSoluong(), input.quantity()));
      khoDao.merge(variant);
      ChiTietTraHang returnLine = new ChiTietTraHang();
      returnLine.setTraHang(returned);
      returnLine.setMaHoaDon(orderId);
      returnLine.setMaBienThe(input.variantId());
      returnLine.setSoLuong(input.quantity());
      returnLine.setDonGiaVon(line.getGiaVon());
      khoDao.persist(returnLine);
      khoDao.saveMovement(
          variant,
          LoaiBienDongKho.KHACH_TRA_HANG.getCode(),
          input.quantity(),
          line.getGiaVon(),
          "ORDER:" + orderId,
          "Nhận hàng khách trả");
    }
  }

  private void updateWeightedCost(
      ChiTietSanPham variant, int incomingQuantity, BigDecimal unitCost) {
    BigDecimal normalizedCost = money(unitCost);
    if (variant.getSoluong() > 0 && variant.getGiaVonBinhQuan() == null) {
      throw new IllegalArgumentException(
          "Cần thiết lập giá vốn tồn đầu kỳ trước khi nhập thêm biến thể đang có tồn.");
    }
    if (variant.getSoluong() == 0 || variant.getGiaVonBinhQuan() == null) {
      variant.setGiaVonBinhQuan(normalizedCost);
      return;
    }
    BigDecimal oldValue =
        variant.getGiaVonBinhQuan().multiply(BigDecimal.valueOf(variant.getSoluong()));
    BigDecimal newValue = normalizedCost.multiply(BigDecimal.valueOf(incomingQuantity));
    BigDecimal average =
        oldValue
            .add(newValue)
            .divide(
                BigDecimal.valueOf((long) variant.getSoluong() + incomingQuantity),
                2,
                RoundingMode.HALF_UP);
    variant.setGiaVonBinhQuan(average);
  }

  private void applyCostedReceipt(
      ChiTietSanPham variant, int incomingQuantity, BigDecimal unitCost) {
    if (unitCost == null) {
      if (variant.getSoluong() > 0 && variant.getGiaVonBinhQuan() != null) {
        throw new IllegalArgumentException(
            "Không thể nhập hàng trả thiếu giá vốn vào tồn đã định giá.");
      }
      variant.setGiaVonBinhQuan(null);
      return;
    }
    updateWeightedCost(variant, incomingQuantity, unitCost);
  }

  private BigDecimal unitPrice(ChiTietHoaDon line) {
    if (line.getGiatien() == null) {
      throw new IllegalArgumentException("Giá bán lưu trên hóa đơn không hợp lệ.");
    }
    try {
      BigDecimal price = new BigDecimal(line.getGiatien());
      if (price.signum() < 0) {
        throw new IllegalArgumentException("Giá bán lưu trên hóa đơn không hợp lệ.");
      }
      return price;
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException("Giá bán lưu trên hóa đơn không hợp lệ.");
    }
  }

  private BigDecimal money(BigDecimal amount) {
    if (amount == null || amount.signum() < 0 || amount.scale() > 2) {
      throw new IllegalArgumentException(
          "Đơn giá/giá trị tiền phải không âm và tối đa 2 chữ số thập phân.");
    }
    return amount.setScale(2, RoundingMode.HALF_UP);
  }

  private String trim(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }
}

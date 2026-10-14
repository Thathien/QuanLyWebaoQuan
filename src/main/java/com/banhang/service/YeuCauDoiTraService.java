package com.banhang.service;

import com.banhang.dao.KhoDao;
import com.banhang.dao.YeuCauDoiTraDao;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.ChiTietYeuCauDoiTra;
import com.banhang.entity.HoaDon;
import com.banhang.entity.LoaiYeuCauDoiTra;
import com.banhang.entity.TrangThaiHoaDon;
import com.banhang.entity.TrangThaiYeuCauDoiTra;
import com.banhang.entity.YeuCauDoiTra;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class YeuCauDoiTraService {
  private static final int REQUEST_WINDOW_DAYS = 30;
  private static final DateTimeFormatter LEGACY_ORDER_DATE =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

  private final KhoDao khoDao;
  private final YeuCauDoiTraDao yeuCauDoiTraDao;

  public YeuCauDoiTraService(KhoDao khoDao, YeuCauDoiTraDao yeuCauDoiTraDao) {
    this.khoDao = khoDao;
    this.yeuCauDoiTraDao = yeuCauDoiTraDao;
  }

  public YeuCauDoiTra createRequest(
      int orderId,
      String customerEmail,
      LoaiYeuCauDoiTra requestType,
      String reason,
      List<RequestLine> requestLines) {
    HoaDon order = khoDao.findOrderForUpdate(orderId);
    if (order == null
        || customerEmail == null
        || order.getEmailkhachhang() == null
        || !order.getEmailkhachhang().equalsIgnoreCase(customerEmail.trim())) {
      throw new IllegalArgumentException("Không tìm thấy đơn hàng.");
    }
    if (!isEligible(order)) {
      throw new IllegalArgumentException(
          "Chỉ có thể yêu cầu đổi/trả trong vòng 30 ngày từ khi đơn hoàn thành.");
    }
    if (requestType == null
        || reason == null
        || reason.isBlank()
        || reason.trim().length() > 255
        || requestLines == null
        || requestLines.isEmpty()) {
      throw new IllegalArgumentException("Thông tin yêu cầu đổi/trả không hợp lệ.");
    }

    Set<Integer> submittedVariants = new HashSet<>();
    YeuCauDoiTra request = new YeuCauDoiTra();
    request.setHoaDon(order);
    request.setLoaiYeuCau(requestType);
    request.setLyDo(reason.trim());
    yeuCauDoiTraDao.persist(request);

    int selectedLines = 0;
    Map<Integer, ChiTietSanPham> replacementVariants = new HashMap<>();
    Map<Integer, Long> replacementQuantities = new HashMap<>();
    for (RequestLine input : requestLines) {
      if (input.quantity() < 0 || !submittedVariants.add(input.variantId())) {
        throw new IllegalArgumentException("Số lượng yêu cầu không hợp lệ.");
      }
      if (input.quantity() == 0) {
        continue;
      }
      ChiTietHoaDon orderLine = khoDao.getOrderLine(orderId, input.variantId());
      if (orderLine == null) {
        throw new IllegalArgumentException("Sản phẩm không thuộc đơn hàng này.");
      }
      long remaining =
          orderLine.getSoluong()
              - khoDao.returnedQuantity(orderId, input.variantId())
              - khoDao.refundedQuantity(orderId, input.variantId())
              - yeuCauDoiTraDao.requestedQuantity(orderId, input.variantId());
      if (input.quantity() > remaining) {
        throw new IllegalArgumentException("Số lượng yêu cầu vượt quá số lượng còn được đổi/trả.");
      }

      ChiTietSanPham original = khoDao.findVariantForUpdate(input.variantId());
      if (original == null) {
        throw new IllegalArgumentException("Không tìm thấy biến thể sản phẩm.");
      }
      ChiTietSanPham replacement = null;
      if (requestType == LoaiYeuCauDoiTra.DOI) {
        if (input.replacementVariantId() == null
            || input.replacementVariantId() == input.variantId()) {
          throw new IllegalArgumentException("Vui lòng chọn biến thể khác để đổi.");
        }
        replacement = khoDao.findVariantForUpdate(input.replacementVariantId());
        if (replacement == null
            || replacement.getSanPham() == null
            || original.getSanPham() == null
            || !Objects.equals(
                replacement.getSanPham().getMasanpham(), original.getSanPham().getMasanpham())
            || !Boolean.FALSE.equals(replacement.getSanPham().getHiden())
            || replacement.getSoluong() < input.quantity()) {
          throw new IllegalArgumentException(
              "Biến thể thay thế phải cùng sản phẩm và còn đủ tồn kho.");
        }
        int replacementId = replacement.getMachitietsanpham();
        replacementVariants.put(replacementId, replacement);
        replacementQuantities.merge(replacementId, (long) input.quantity(), Long::sum);
      }

      ChiTietYeuCauDoiTra line = new ChiTietYeuCauDoiTra();
      line.setYeuCau(request);
      line.setBienThe(original);
      line.setBienTheThayThe(replacement);
      line.setSoLuong(input.quantity());
      request.getChiTiet().add(line);
      yeuCauDoiTraDao.persist(line);
      selectedLines++;
    }
    if (selectedLines == 0) {
      throw new IllegalArgumentException("Hãy chọn ít nhất một sản phẩm cần đổi/trả.");
    }
    for (Map.Entry<Integer, Long> demand : replacementQuantities.entrySet()) {
      if (demand.getValue() > replacementVariants.get(demand.getKey()).getSoluong()) {
        throw new IllegalArgumentException(
            "Số lượng biến thể thay thế vượt quá số lượng còn trong kho.");
      }
    }
    yeuCauDoiTraDao.flush();
    return request;
  }

  @Transactional(readOnly = true)
  public List<YeuCauDoiTra> getRequestsForOrder(int orderId) {
    return yeuCauDoiTraDao.findByOrder(orderId);
  }

  @Transactional(readOnly = true)
  public List<YeuCauDoiTra> getAllRequests() {
    return yeuCauDoiTraDao.findAll();
  }

  @Transactional(readOnly = true)
  public boolean isEligible(HoaDon order) {
    return order != null
        && order.getTinhtrang() == TrangThaiHoaDon.HOAN_THANH.getCode()
        && isWithinRequestWindow(order, LocalDateTime.now());
  }

  public void updateRequestStatus(long requestId, TrangThaiYeuCauDoiTra newStatus) {
    if (newStatus != TrangThaiYeuCauDoiTra.CHAP_NHAN
        && newStatus != TrangThaiYeuCauDoiTra.TU_CHOI) {
      throw new IllegalArgumentException("Trạng thái xử lý yêu cầu không hợp lệ.");
    }
    YeuCauDoiTra request = yeuCauDoiTraDao.findForUpdate(requestId);
    if (request == null || request.getTrangThai() != TrangThaiYeuCauDoiTra.CHO_XU_LY) {
      throw new IllegalArgumentException("Yêu cầu không tồn tại hoặc đã được xử lý.");
    }
    request.setTrangThai(newStatus);
  }

  private boolean isWithinRequestWindow(HoaDon order, LocalDateTime now) {
    LocalDateTime completedAt = order.getCompletedAt();
    if (completedAt == null) {
      completedAt = order.getCreatedAt();
    }
    if (completedAt == null && order.getNgaylap() != null) {
      try {
        completedAt = LocalDateTime.parse(order.getNgaylap(), LEGACY_ORDER_DATE);
      } catch (DateTimeParseException ignored) {
        return false;
      }
    }
    return completedAt != null
        && !completedAt.isAfter(now)
        && !completedAt.isBefore(now.minusDays(REQUEST_WINDOW_DAYS));
  }

  public record RequestLine(int variantId, int quantity, Integer replacementVariantId) {}
}

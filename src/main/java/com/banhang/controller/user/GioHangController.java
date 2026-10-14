package com.banhang.controller.user;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.GioHang;
import com.banhang.entity.HoaDon;
import com.banhang.service.ChiTietHoaDonService;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.GioHangService;
import com.banhang.service.HoaDonService;
import com.banhang.service.KhoService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
@RequiredArgsConstructor
public class GioHangController {
  private static final Logger LOGGER = Logger.getLogger(GioHangController.class.getName());

  private final GioHangService gioHangService;
  private final HoaDonService hoaDonService;
  private final ChiTietHoaDonService chiTietHoaDonService;
  private final ChiTietSanPhamService chiTietSanPhamService;
  private final KhoService khoService;
  private final JavaMailSender mailsender;

  @GetMapping("themgiohang")
  @ResponseBody
  public String themgiohang(
      @RequestParam int machitietsanpham,
      @RequestParam int masp,
      @RequestParam int masize,
      @RequestParam int mamau,
      @RequestParam String tensp,
      @RequestParam String giatien,
      @RequestParam String tenmau,
      @RequestParam String tensize,
      @RequestParam int soluong,
      HttpSession httpSession,
      ModelMap map) {
    if (soluong < 1 || soluong > 99) {
      return "false";
    }
    ChiTietSanPham variant = findVariant(masp, machitietsanpham, masize, mamau);
    if (variant == null
        || variant.getSanPham() == null
        || !Boolean.FALSE.equals(variant.getSanPham().getHiden())
        || variant.getSoluong() < soluong
        || !isValidPrice(variant.getSanPham().getGiatien())) {
      return "false";
    }
    int existingQuantity =
        gioHangService.getCart(httpSession).stream()
            .filter(
                item ->
                    item.getMachitietsanpham() == machitietsanpham
                        || (item.getMasp() == masp
                            && item.getMasize() == masize
                            && item.getMamau() == mamau))
            .mapToInt(GioHang::getSoluong)
            .sum();
    if (existingQuantity + soluong > variant.getSoluong()) {
      return "false";
    }
    gioHangService.addItem(
        machitietsanpham,
        masp,
        masize,
        mamau,
        variant.getSanPham().getTensanpham(),
        variant.getSanPham().getGiatien(),
        variant.getMauSanPham().getTenmau(),
        variant.getSizeSanPham().getSize(),
        soluong,
        httpSession);
    map.addAttribute("giohang", gioHangService.getCart(httpSession));
    return "true";
  }

  @GetMapping("giohang")
  public String loadGioHang(HttpSession httpSession, ModelMap map) {
    int temp = checkSecurityUser(httpSession);
    if (temp == 1) {
      return "redirect:/admin";
    } else {
      if (gioHangService.hasCart(httpSession)) {
        List<GioHang> gioHangs = gioHangService.getCart(httpSession);
        boolean isEmpty = gioHangs.isEmpty();
        map.addAttribute("isEmpty", isEmpty);
        map.addAttribute("soluonggiohang", gioHangs.size());
        map.addAttribute("gioHangs", gioHangs);
      } else {
        map.addAttribute("isEmpty", true);
        map.addAttribute("gioHangs", List.of());
      }
      return "user/cart_user";
    }
  }

  @GetMapping("giohang/delete/")
  @ResponseBody
  public String xoaSanPhamKhoiGio(
      @RequestParam int masp,
      @RequestParam int mamau,
      @RequestParam int masize,
      HttpSession httpSession,
      ModelMap map) {
    gioHangService.removeItem(masp, masize, mamau, httpSession);
    return "true";
  }

  @GetMapping("giohang/update/")
  @ResponseBody
  public String upadateSanPhamKhoiGio(
      @RequestParam int masp,
      @RequestParam int masize,
      @RequestParam int mamau,
      @RequestParam int soluong,
      HttpSession httpSession,
      ModelMap map) {
    if (soluong < 1 || soluong > 99) {
      return "false";
    }
    GioHang item =
        gioHangService.getCart(httpSession).stream()
            .filter(
                cartItem ->
                    cartItem.getMasp() == masp
                        && cartItem.getMasize() == masize
                        && cartItem.getMamau() == mamau)
            .findFirst()
            .orElse(null);
    if (item == null) {
      return "false";
    }
    ChiTietSanPham variant = findVariant(masp, item.getMachitietsanpham(), masize, mamau);
    if (variant == null || variant.getSoluong() < soluong) {
      return "false";
    }
    gioHangService.updateQuantity(masp, masize, mamau, soluong, httpSession);
    return "true";
  }

  @PostMapping("giohang")
  @ResponseBody
  @Transactional
  public String themHoaDon(
      @RequestParam String tenkhachhang,
      @RequestParam String emailkhachang,
      @RequestParam String sodienthoai,
      @RequestParam String hinhthucgiao,
      @RequestParam String diachigiaohang,
      @RequestParam String ghichu,
      HttpSession httpSession)
      throws MessagingException {
    if (null != httpSession.getAttribute("giohang")) {
      List<GioHang> listGioHangs = gioHangService.getCart(httpSession);
      if (listGioHangs == null
          || listGioHangs.isEmpty()
          || !isValidCheckout(
              tenkhachhang,
              emailkhachang,
              sodienthoai,
              hinhthucgiao,
              diachigiaohang,
              ghichu,
              listGioHangs)) {
        return "false";
      }
      HoaDon hoaDon = new HoaDon();
      hoaDon.setTenkhachhang(tenkhachhang.trim());
      hoaDon.setEmailkhachhang(emailkhachang.trim());
      hoaDon.setSdt(sodienthoai.trim());
      hoaDon.setDiachigiaohang(diachigiaohang.trim());
      hoaDon.setGhichu(ghichu == null ? "" : ghichu.trim());
      String hinhthucgiaohang;
      if (hinhthucgiao.equals("0")) {
        hinhthucgiaohang = "Giao hàng tận nơi";
      } else {
        hinhthucgiaohang = "Nhận tại cửa hàng";
      }
      hoaDon.setHinhthucgiaohang(hinhthucgiaohang);
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = new Date();
      String ngaylap = formatter.format(date);
      hoaDon.setNgaylap(ngaylap);
      hoaDon.setCreatedAt(LocalDateTime.now());
      hoaDon.setTinhtrang(0);
      int idHoaDon = hoaDonService.addNewHoaDonUser(hoaDon);
      if (idHoaDon > 0) {
        for (int i = 0; i < listGioHangs.size(); i++) {
          ChiTietHoaDonId chiTietHoaDonId = new ChiTietHoaDonId();
          chiTietHoaDonId.setMahoadon(idHoaDon);
          chiTietHoaDonId.setMachitietsanpham(listGioHangs.get(i).getMachitietsanpham());
          ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
          chiTietHoaDon.setChiTietHoaDonId(chiTietHoaDonId);
          chiTietHoaDon.setSoluong(listGioHangs.get(i).getSoluong());
          chiTietHoaDon.setGiatien(listGioHangs.get(i).getGiatien());
          if (chiTietHoaDonService.addChiTietHoaDon(chiTietHoaDon) <= 0) {
            throw new IllegalStateException("Không thể lưu chi tiết hóa đơn.");
          }
          ChiTietSanPham variant =
              findVariant(
                  listGioHangs.get(i).getMasp(),
                  listGioHangs.get(i).getMachitietsanpham(),
                  listGioHangs.get(i).getMasize(),
                  listGioHangs.get(i).getMamau());
          chiTietHoaDon.setGiaVon(variant.getGiaVonBinhQuan());
          variant.setSoluong(variant.getSoluong() - listGioHangs.get(i).getSoluong());
          if (!chiTietSanPhamService.updateCTSanPham(variant)) {
            throw new IllegalStateException("Không thể cập nhật tồn kho sản phẩm.");
          }
          khoService.recordSale(variant, listGioHangs.get(i).getSoluong(), idHoaDon);
        }
        StringBuilder temp = new StringBuilder();
        double tongtien = 0;
        for (int i = 0; i < listGioHangs.size(); i++) {
          int masp = listGioHangs.get(i).getMasp();
          String tensp = listGioHangs.get(i).getTensp();
          String sosize = listGioHangs.get(i).getTensize();
          String somau = listGioHangs.get(i).getTenmau();
          int soluong = listGioHangs.get(i).getSoluong();
          String giasp = listGioHangs.get(i).getGiatien();
          long thanhtien = (long) soluong * Long.parseLong(giasp);
          tongtien += thanhtien;
          temp.append("<tr><td>")
              .append(masp)
              .append("</td><td>")
              .append(html(tensp))
              .append("</td><td>")
              .append(html(sosize))
              .append("</td><td>")
              .append(html(somau))
              .append("</td><td>")
              .append(soluong)
              .append("</td><td>")
              .append(html(giasp))
              .append(" VNĐ</td><td>")
              .append(thanhtien)
              .append(" VNĐ</td></tr>");
        }
        try {
          MimeMessage message = mailsender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
          String messinfor =
              "<h1>Xin chào: "
                  + html(tenkhachhang)
                  + "</h1>"
                  + "<h2>Cảm ơn bạn đã đặt hàng tại cửa hàng chúng tôi.</h2>"
                  + "<h3>Mã hóa đơn: "
                  + idHoaDon
                  + "</h3><h3>Ngày đặt: "
                  + ngaylap
                  + "</h3><h3>Hình thức giao hàng: "
                  + html(hinhthucgiaohang)
                  + "</h3><h3>Số điện thoại liên hệ: "
                  + html(sodienthoai)
                  + "</h3><h3>Ghi chú: "
                  + html(ghichu)
                  + "</h3><table><caption>Thông tin sản phẩm</caption><thead><tr>"
                  + "<th>Mã sản phẩm</th><th>Tên sản phẩm</th><th>Size</th><th>Màu</th>"
                  + "<th>Số lượng</th><th>Giá</th><th>Thành tiền</th></tr></thead><tbody>"
                  + temp
                  + "</tbody></table><h2>Tổng tiền: "
                  + (long) tongtien
                  + " VNĐ</h2>";
          helper.setText(messinfor, true);
          helper.setSubject("Xác nhận đơn hàng #" + idHoaDon);
          helper.setTo(emailkhachang.trim());
          mailsender.send(message);
        } catch (MessagingException | MailException exception) {
          LOGGER.warning(
              "Order "
                  + idHoaDon
                  + " saved, but confirmation email was not sent: "
                  + exception.getMessage());
          httpSession.removeAttribute("giohang");
          return "email-failed";
        }
        httpSession.removeAttribute("giohang");
        return "true";
      }
    }
    return "false";
  }

  private ChiTietSanPham findVariant(int productId, int detailId, int sizeId, int colorId) {
    return chiTietSanPhamService.getInforCTSanPhambyID(productId).stream()
        .filter(detail -> detail.getMachitietsanpham() == detailId)
        .filter(
            detail ->
                detail.getSizeSanPham() != null && detail.getSizeSanPham().getMasize() == sizeId)
        .filter(
            detail ->
                detail.getMauSanPham() != null && detail.getMauSanPham().getMamau() == colorId)
        .findFirst()
        .orElse(null);
  }

  private boolean isValidPrice(String price) {
    return price != null && price.matches("\\d{1,15}");
  }

  private boolean isValidCheckout(
      String customerName,
      String email,
      String phone,
      String deliveryType,
      String address,
      String note,
      List<GioHang> cart) {
    if (customerName == null
        || customerName.isBlank()
        || customerName.trim().length() > 50
        || email == null
        || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        || phone == null
        || !phone.matches("^(09|03|07|08|05)\\d{8}$")
        || address == null
        || address.isBlank()
        || address.length() > 255
        || (note != null && note.length() > 50)
        || !("0".equals(deliveryType) || "1".equals(deliveryType))) {
      return false;
    }
    for (GioHang item : cart) {
      if (item.getSoluong() < 1 || item.getSoluong() > 99) {
        return false;
      }
      ChiTietSanPham variant =
          findVariant(
              item.getMasp(), item.getMachitietsanpham(), item.getMasize(), item.getMamau());
      if (variant == null
          || variant.getSoluong() < item.getSoluong()
          || variant.getSanPham() == null
          || !Boolean.FALSE.equals(variant.getSanPham().getHiden())
          || !isValidPrice(variant.getSanPham().getGiatien())) {
        return false;
      }
      item.setTensp(variant.getSanPham().getTensanpham());
      item.setGiatien(variant.getSanPham().getGiatien());
      item.setTenmau(variant.getMauSanPham().getTenmau());
      item.setTensize(variant.getSizeSanPham().getSize());
    }
    return true;
  }

  private String html(String value) {
    return org.springframework.web.util.HtmlUtils.htmlEscape(value == null ? "" : value);
  }

  public int checkSecurityUser(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      return taiKhoanLogin.getMachucvu();
    }
    return 0;
  }
}

package com.banhang.controller.user;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.GioHang;
import com.banhang.entity.HoaDon;
import com.banhang.service.ChiTietHoaDonService;
import com.banhang.service.GioHangService;
import com.banhang.service.HoaDonService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
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
  private final GioHangService gioHangService;
  private final HoaDonService hoaDonService;
  private final ChiTietHoaDonService chiTietHoaDonService;
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
    gioHangService.addItem(
        machitietsanpham,
        masp,
        masize,
        mamau,
        tensp,
        giatien,
        tenmau,
        tensize,
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
      }
      return "cart_user";
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
    gioHangService.updateQuantity(masp, masize, mamau, soluong, httpSession);
    return "true";
  }

  //
  //	@GetMapping("giohang/update/")
  //	@ResponseBody
  //	public Boolean upadateSanPhamKhoiGio(@RequestParam int machitietsanpham,@RequestParam int
  // soluong,HttpSession httpSession,ModelMap map) {
  //		List<GioHang> listGioHangs=(List<GioHang>) httpSession.getAttribute("giohang");
  ////		int vitri=kiemTraSanPhamTonTai(masp, masize, mamau, httpSession);
  //		int vitri=kiemTraSanPhamTonTaiTheoMaChiTiet(machitietsanpham, httpSession);
  //		listGioHangs.get(vitri).setSoluong(soluong);
  //		return true;
  //	}
  @PostMapping("giohang")
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
      HoaDon hoaDon = new HoaDon();
      hoaDon.setTenkhachhang(tenkhachhang);
      hoaDon.setSdt(sodienthoai);
      hoaDon.setDiachigiaohang(diachigiaohang);
      hoaDon.setGhichu(ghichu);
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
      hoaDon.setTinhtrang(0);
      int idHoaDon = hoaDonService.addNewHoaDonUser(hoaDon);
      if (idHoaDon > 0) {
        Set<ChiTietHoaDon> listChiTietHoaDons = new HashSet<>();
        for (int i = 0; i < listGioHangs.size(); i++) {
          ChiTietHoaDonId chiTietHoaDonId = new ChiTietHoaDonId();
          chiTietHoaDonId.setMachitietsanpham(listGioHangs.get(i).getMachitietsanpham());
          ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
          chiTietHoaDon.setChiTietHoaDonId(chiTietHoaDonId);
          chiTietHoaDon.setGiatien(listGioHangs.get(i).getGiatien());
          chiTietHoaDonService.addChiTietHoaDon(chiTietHoaDon);
        }
        String temp = "";
        double tongtien = 0;
        for (int i = 0; i < listGioHangs.size(); i++) {
          int masp = listGioHangs.get(i).getMasp();
          String tensp = listGioHangs.get(i).getTensp();
          String sosize = listGioHangs.get(i).getTensize();
          String somau = listGioHangs.get(i).getTenmau();
          int soluong = listGioHangs.get(i).getSoluong();
          String giasp = listGioHangs.get(i).getGiatien();
          String thanhtien = String.valueOf(soluong * Integer.parseInt(giasp));
          tongtien += (soluong * Integer.parseInt(giasp));
          temp +=
              "<tr>"
                  + "<td>"
                  + masp
                  + "</td>"
                  + "<td>"
                  + tensp
                  + "</td>"
                  + "<td>"
                  + sosize
                  + "</td>"
                  + "<td>"
                  + somau
                  + "</td>"
                  + "<td>"
                  + soluong
                  + "</td>"
                  + "<td>"
                  + giasp
                  + ",000 VNĐ"
                  + "</td>"
                  + "<td>"
                  + thanhtien
                  + ",000 VNĐ"
                  + "</td>"
                  + "</tr>"
                  + "/n";
        }
        MimeMessage message = mailsender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        String messinfor =
            "<h1>Hello: "
                + tenkhachhang
                + "</h1>"
                + "<h2>Cảm ơn bạn đã tin dùng mua hàng của chúng tôi </h2>"
                + "<h3 style ='color:red'>Mã hóa đơn:  "
                + idHoaDon
                + "</h3>"
                + "<h3 style ='color:red'>Ngày đặt: "
                + ngaylap
                + "</h3>"
                + "<h3>Hình thức giao hàng:  "
                + hinhthucgiaohang
                + "</h3>"
                + "<h3>Số điện thoại liên hệ:  "
                + sodienthoai
                + "</h3>"
                + "<h3>Ghi chú:  "
                + ghichu
                + "</h3>"
                + "</br>"
                + "<table>"
                + "<caption> Thông tin sản phẩm</caption>"
                + "<thead>"
                + "<tr>"
                + "<th>Mã sản phẩm</th>"
                + "<th>Tên sản phẩmm</th>"
                + "<th>Số Size</th>"
                + "<th>Màu</th>"
                + "<th>Số lượng mua</th>"
                + "<th>Giá sản phẩm</th>"
                + "<th>Thành tiền</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>"
                + temp
                + "</tbody>"
                + "</table>"
                + "<h2 style ='color:red'>Tổng tiền:  "
                + tongtien
                + ",000 VNĐ"
                + "</h2>";
        message.setContent(messinfor, "text/html");
        helper.setSubject("Cảm ơn bạn đã mua tin tưởng đặt hàng chúng tôi!");
        helper.setTo(emailkhachang);

        mailsender.send(message);
        return "true";
      }
    }
    return "fasle";
  }

  public int checkSecurityUser(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      return taiKhoanLogin.getMachucvu();
    }
    return 0;
  }
}

package com.banhang.controller.user;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangKyModel;
import com.banhang.model.DangNhapModel;
import com.banhang.service.ChucVuService;
import com.banhang.service.NhanVienService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
@RequiredArgsConstructor
public class TaiKhoanUserController {
  private final JavaMailSender mailsender;
  private final NhanVienService nhanVienService;
  private final ChucVuService chucVuService;

  @GetMapping("dangnhap")
  public String showdangnhap(Model m, HttpSession httpSession) {
    int temp = checkSecurityUser(httpSession);
    if (temp == 0) {
      m.addAttribute("dangnhapuser", new DangNhapModel());
      return "login_user";
    } else if (temp == 1) {
      return "redirect:/admin";
    } else {
      return "redirect:/";
    }
  }

  @PostMapping("dangnhap")
  public String dangnhapProcess(
      @Valid @ModelAttribute("dangnhapuser") DangNhapModel dangnhapuser,
      BindingResult br,
      ModelMap map) {
    if (br.hasErrors()) {
      return "login_user";
    } else {

      //			boolean
      // temp=nhanVienService.checkUser(dangnhapuser.getEmail(),dangnhapuser.getMatKhau());
      //			System.out.println("KQ: "+ temp);
      //			if(temp==true) {
      //				NhanVien tk= new NhanVien();
      //				tk=nhanVienService.getInforby_User_Pass(dangnhapuser.getEmail(),
      // dangnhapuser.getMatKhau());
      //				TaiKhoanLogin taikhoan_session= new TaiKhoanLogin();
      //				taikhoan_session.setManhanvien(tk.getManhanvien());
      //				taikhoan_session.setHoten(tk.getHoten());
      //				taikhoan_session.setTendangnhap(tk.getTendangnhap());
      //				taikhoan_session.setMatkhau(tk.getMatkhau());
      //				taikhoan_session.setMachucvu(tk.getChucVu().getMachucvu());
      //				map.addAttribute("taikhoan",taikhoan_session);
      ////				httpSession.setAttribute("taikhoan", taikhoan_session););
      //				return "redirect:/";
      //			}else {
      //				map.addAttribute("resultDangNhap","Tài khoản hoặc mật khẩu không hơp lệ");
      //				return "login_user";
      //			}
      //

      NhanVien nhanVien =
          nhanVienService.getInforby_User_Pass(dangnhapuser.getEmail(), dangnhapuser.getMatKhau());
      if (nhanVien != null) {
        NhanVien tk = nhanVien;
        TaiKhoanLogin taikhoan_session = new TaiKhoanLogin();
        taikhoan_session.setManhanvien(tk.getManhanvien());
        taikhoan_session.setHoten(tk.getHoten());
        taikhoan_session.setTendangnhap(tk.getTendangnhap());
        taikhoan_session.setMatkhau(tk.getMatkhau());
        taikhoan_session.setMachucvu(tk.getChucVu().getMachucvu());
        map.addAttribute("taikhoan", taikhoan_session);
        //				httpSession.setAttribute("taikhoan", taikhoan_session););
        return "redirect:/";
      } else {
        map.addAttribute("resultDangNhap", "Tài khoản hoặc mật khẩu không hơp lệ");
        return "login_user";
      }
    }
  }

  @GetMapping("logout")
  public String logout(HttpSession httpSession, Model model) {
    httpSession.removeAttribute("taikhoan");
    httpSession.invalidate();
    if (model.containsAttribute("taikhoan")) {
      model.asMap().remove("taikhoan");
    }
    return "redirect:/";
  }

  @GetMapping("dangky")
  public String showdangky(Model m) {
    m.addAttribute("dangkyuser", new DangKyModel());
    return "regester_user";
  }

  @PostMapping("dangky")
  public String dangkyProcess(
      @Valid @ModelAttribute("dangkyuser") DangKyModel dangkyuser, BindingResult br, ModelMap map)
      throws MessagingException {
    if (br.hasErrors()) {
      return "regester_user";
    }

    String hoten = dangkyuser.getHotendk() != null ? dangkyuser.getHotendk().trim() : "";
    String email = dangkyuser.getEmaildk() != null ? dangkyuser.getEmaildk().trim() : "";
    String diachi = dangkyuser.getDiachidk() != null ? dangkyuser.getDiachidk().trim() : "";
    String matkhau = dangkyuser.getMatkhaudk() != null ? dangkyuser.getMatkhaudk().trim() : "";
    String nhaplaiMk =
        dangkyuser.getNhaplaimkdk() != null ? dangkyuser.getNhaplaimkdk().trim() : "";
    String gioitinh = dangkyuser.getGioitinhdk();

    if (hoten.isEmpty()) {
      map.addAttribute("error_hotendk", "Họ tên nhập không hợp lệ");
      return "regester_user";
    }
    if (email.isEmpty()) {
      map.addAttribute("error_emaildk", "Nhập email không hợp lệ");
      return "regester_user";
    }
    if (diachi.isEmpty()) {
      map.addAttribute("error_diachidk", "Địa chỉ không hợp lệ");
      return "regester_user";
    }
    if (matkhau.isEmpty()) {
      map.addAttribute("error_matkhaudk", "Mật khẩu không hợp lệ");
      return "regester_user";
    }
    if (gioitinh == null || "null".equals(gioitinh)) {
      map.addAttribute("error_gioitinhdk", "Bạn chưa chọn giới tính");
      return "regester_user";
    }
    if (nhaplaiMk.isEmpty()) {
      map.addAttribute("error_nhaplaimatkhaudk", "Nhập lại mật khẩu không hợp lệ");
      return "regester_user";
    }
    if (!matkhau.equals(nhaplaiMk)) {
      map.addAttribute("error_nhaplaimatkhaudk", "Nhập lại mật khẩu không đúng");
      return "regester_user";
    }
    if (nhanVienService.checkEmail(email)) {
      map.addAttribute("error_emaildk", "Email trùng yêu cầu nhập email khác");
      return "regester_user";
    }

    NhanVien nv = new NhanVien();
    nv.setHoten(hoten);
    nv.setDiachi(diachi);
    nv.setGioitinh(!"0".equals(gioitinh));
    nv.setEmail(email);
    nv.setTendangnhap(email);
    nv.setMatkhau(matkhau);
    nv.setCmnd(null);
    nv.setChucVu(chucVuService.getById(3)); // Role 3 is Customer (User)
    nv.setLock(false);
    nv.setXacthuc(false);

    int id = nhanVienService.register(nv);
    if (id != 0) {
      map.addAttribute("success_fail", "Đăng ký thành công hãy truy cập email của bạn để xác thực");

      MimeMessage message = mailsender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
      String messinfor = "Vui lòng nhấn vào link này để xác thực đăng ký ";
      message.setContent(messinfor, "text/html");
      helper.setSubject("Yêu cầu xác thực email bạn đã đăng ký");
      helper.setTo(email);
      mailsender.send(message);
    } else {
      map.addAttribute("success_fail", "Đăng ký không thành công");
    }
    return "regester_user";
  }

  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  public static boolean validate(String emailStr) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
    return matcher.find();
  }

  // admin -> 1  // other  -> 0
  public int checkSecurityUser(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute("taikhoan");
    if (taiKhoanLogin != null) {
      return taiKhoanLogin.getMachucvu();
    }
    return 0;
  }
}

package com.banhang.controller.user;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.HoaDon;
import com.banhang.entity.LoaiYeuCauDoiTra;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangKyModel;
import com.banhang.model.DangNhapModel;
import com.banhang.service.ChiTietSanPhamService;
import com.banhang.service.ChucVuService;
import com.banhang.service.HoaDonService;
import com.banhang.service.KhoService;
import com.banhang.service.NhanVienService;
import com.banhang.service.YeuCauDoiTraService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
@RequiredArgsConstructor
public class TaiKhoanUserController {
  private static final String ACCOUNT_ATTRIBUTE = "taikhoan";
  private static final String LOGIN_FORM_ATTRIBUTE = "dangnhapuser";
  private static final String REGISTER_FORM_ATTRIBUTE = "dangkyuser";

  private static final String LOGIN_VIEW = "user/login_user";
  private static final String REGISTER_VIEW = "user/regester_user";

  private static final int NO_ROLE = 0;
  private static final int ADMIN_ROLE = 1;
  private static final int CUSTOMER_ROLE = 3;
  private static final int VERIFICATION_TOKEN_BYTES = 32;
  private static final int VERIFICATION_EXPIRY_HOURS = 24;
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private final JavaMailSender mailsender;
  private final NhanVienService nhanVienService;
  private final ChucVuService chucVuService;
  private final HoaDonService hoaDonService;
  private final KhoService khoService;
  private final ChiTietSanPhamService chiTietSanPhamService;
  private final YeuCauDoiTraService yeuCauDoiTraService;

  @Value("${app.base-url:http://localhost:8080}")
  private String baseUrl;

  @GetMapping("dangnhap")
  public String showDangNhap(Model model, HttpSession httpSession) {
    int role = checkSecurityUser(httpSession);
    if (role == NO_ROLE) {
      model.addAttribute(LOGIN_FORM_ATTRIBUTE, new DangNhapModel());
      return LOGIN_VIEW;
    }
    if (role == ADMIN_ROLE) {
      return "redirect:/admin";
    }
    return "redirect:/";
  }

  @PostMapping("dangnhap")
  public String dangNhapProcess(
      @Valid @ModelAttribute(LOGIN_FORM_ATTRIBUTE) DangNhapModel dangNhapUser,
      BindingResult bindingResult,
      ModelMap modelMap) {
    if (bindingResult.hasErrors()) {
      return LOGIN_VIEW;
    }

    NhanVien nhanVien =
        nhanVienService.getCustomerByCredentials(
            dangNhapUser.getEmail(), dangNhapUser.getMatKhau());
    if (nhanVien == null
        || nhanVien.getChucVu() == null
        || nhanVien.getChucVu().getMachucvu() != CUSTOMER_ROLE) {
      modelMap.addAttribute("resultDangNhap", "Tài khoản hoặc mật khẩu không hợp lệ");
      return LOGIN_VIEW;
    }

    modelMap.addAttribute(ACCOUNT_ATTRIBUTE, buildLoginSession(nhanVien));
    return "redirect:/";
  }

  @GetMapping("logout")
  public String logout(HttpSession httpSession, Model model) {
    httpSession.removeAttribute(ACCOUNT_ATTRIBUTE);
    httpSession.invalidate();
    if (model.containsAttribute(ACCOUNT_ATTRIBUTE)) {
      model.asMap().remove(ACCOUNT_ATTRIBUTE);
    }
    return "redirect:/";
  }

  @GetMapping("dangky")
  public String showDangKy(Model model) {
    model.addAttribute(REGISTER_FORM_ATTRIBUTE, new DangKyModel());
    return REGISTER_VIEW;
  }

  @PostMapping("dangky")
  public String dangKyProcess(
      @Valid @ModelAttribute(REGISTER_FORM_ATTRIBUTE) DangKyModel dangKyUser,
      BindingResult bindingResult,
      ModelMap modelMap)
      throws MessagingException {
    if (bindingResult.hasErrors() || !validateRegistration(dangKyUser, modelMap)) {
      return REGISTER_VIEW;
    }

    NhanVien customer = buildCustomer(dangKyUser);
    customer.setTokenXacThuc(generateVerificationToken());
    customer.setThoiHanXacThuc(LocalDateTime.now().plusHours(VERIFICATION_EXPIRY_HOURS));
    int id = nhanVienService.register(customer);
    if (id == 0) {
      modelMap.addAttribute("success_fail", "Đăng ký không thành công");
      return REGISTER_VIEW;
    }

    try {
      sendVerificationEmail(customer.getEmail(), customer.getTokenXacThuc());
      modelMap.addAttribute(
          "success_fail", "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.");
    } catch (MessagingException | MailException exception) {
      modelMap.addAttribute(
          "success_fail",
          "Tài khoản đã được tạo nhưng không gửi được email xác thực. Vui lòng liên hệ quản trị viên.");
    }
    return REGISTER_VIEW;
  }

  @GetMapping("xac-thuc-email")
  public String verifyEmail(@RequestParam(required = false) String token, Model model) {
    boolean verified =
        token != null
            && !token.isBlank()
            && nhanVienService.verifyEmailToken(token, LocalDateTime.now());
    model.addAttribute(
        "verificationResult",
        verified
            ? "Xác thực email thành công. Bạn có thể đăng nhập."
            : "Liên kết xác thực không hợp lệ hoặc đã hết hạn.");
    return "user/xac_thuc_email_user";
  }

  @GetMapping("tai-khoan")
  public String accountPage(HttpSession session, Model model) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    NhanVien customer = nhanVienService.getInforbyId(account.getManhanvien());
    if (customer == null) {
      session.removeAttribute(ACCOUNT_ATTRIBUTE);
      return "redirect:/dangnhap";
    }
    model.addAttribute("customer", customer);
    return "user/account_user";
  }

  @PostMapping("tai-khoan")
  public String updateAccount(
      @RequestParam String hoten, @RequestParam String diachi, HttpSession session, Model model) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }

    NhanVien customer = nhanVienService.getInforbyId(account.getManhanvien());
    String normalizedName = trimToEmpty(hoten);
    String normalizedAddress = trimToEmpty(diachi);
    if (customer == null
        || normalizedName.isEmpty()
        || normalizedName.length() > 50
        || normalizedAddress.isEmpty()
        || normalizedAddress.length() > 255) {
      model.addAttribute("accountError", "Vui lòng nhập họ tên và địa chỉ hợp lệ.");
      model.addAttribute("customer", customer);
      return "user/account_user";
    }

    customer.setHoten(normalizedName);
    customer.setDiachi(normalizedAddress);
    if (!nhanVienService.updateInfor(customer)) {
      model.addAttribute("accountError", "Không thể cập nhật thông tin tài khoản.");
    } else {
      account.setHoten(normalizedName);
      model.addAttribute("accountResult", "Đã cập nhật thông tin tài khoản.");
    }
    model.addAttribute("customer", customer);
    return "user/account_user";
  }

  @PostMapping("tai-khoan/doi-mat-khau")
  public String changePassword(
      @RequestParam String matKhauHienTai,
      @RequestParam String matKhauMoi,
      @RequestParam String xacNhanMatKhauMoi,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    if (matKhauMoi.length() < 8
        || matKhauMoi.length() > 100
        || !matKhauMoi.equals(xacNhanMatKhauMoi)
        || matKhauMoi.equals(matKhauHienTai)) {
      redirectAttributes.addFlashAttribute(
          "accountError",
          "Mật khẩu mới phải từ 8 đến 100 ký tự, khác mật khẩu hiện tại và khớp xác nhận.");
      return "redirect:/tai-khoan";
    }
    if (!nhanVienService.changeCustomerPassword(
        account.getManhanvien(), matKhauHienTai, matKhauMoi)) {
      redirectAttributes.addFlashAttribute(
          "accountError", "Mật khẩu hiện tại không đúng hoặc không thể cập nhật mật khẩu.");
      return "redirect:/tai-khoan";
    }
    redirectAttributes.addFlashAttribute("accountResult", "Đã đổi mật khẩu thành công.");
    return "redirect:/tai-khoan";
  }

  @GetMapping("don-hang")
  public String orderHistory(HttpSession session, Model model) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    model.addAttribute("orders", hoaDonService.getListHoaDonByEmail(account.getTendangnhap()));
    return "user/orders_user";
  }

  @GetMapping("don-hang/{id}")
  public String orderDetails(@PathVariable int id, HttpSession session, Model model) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    NhanVien customer = getCustomerDetails(account, session);
    if (customer == null) {
      return "redirect:/dangnhap";
    }
    HoaDon order = hoaDonService.getCustomerOrderById(id, customer.getEmail());
    if (order == null) {
      return "404";
    }
    model.addAttribute("order", order);
    model.addAttribute("returnRequests", yeuCauDoiTraService.getRequestsForOrder(id));
    model.addAttribute("canRequestReturn", yeuCauDoiTraService.isEligible(order));
    return "user/order-details_user";
  }

  @PostMapping("don-hang/{id}/huy")
  public String cancelOrder(
      @PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    NhanVien customer = getCustomerDetails(account, session);
    if (customer == null) {
      return "redirect:/dangnhap";
    }
    try {
      khoService.cancelCustomerOrder(id, customer.getEmail());
      redirectAttributes.addFlashAttribute("orderResult", "Đã hủy đơn hàng.");
    } catch (IllegalArgumentException exception) {
      redirectAttributes.addFlashAttribute("orderError", exception.getMessage());
    }
    return "redirect:/don-hang";
  }

  @GetMapping("don-hang/{id}/doi-tra")
  public String showReturnRequest(@PathVariable int id, HttpSession session, ModelMap model) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    NhanVien customer = getCustomerDetails(account, session);
    if (customer == null) {
      return "redirect:/dangnhap";
    }
    HoaDon order = hoaDonService.getCustomerOrderById(id, customer.getEmail());
    if (order == null) {
      return "404";
    }
    if (!yeuCauDoiTraService.isEligible(order)) {
      model.addAttribute(
          "orderError", "Chỉ có thể yêu cầu đổi/trả trong vòng 30 ngày từ khi đơn hoàn thành.");
      model.addAttribute("order", order);
      model.addAttribute("returnRequests", yeuCauDoiTraService.getRequestsForOrder(id));
      model.addAttribute("canRequestReturn", false);
      return "user/order-details_user";
    }
    model.addAttribute("order", order);
    model.addAttribute("replacementVariants", getReplacementVariants(order));
    return "user/return-request_user";
  }

  @PostMapping("don-hang/{id}/doi-tra")
  public String createReturnRequest(
      @PathVariable int id,
      @RequestParam String loaiYeuCau,
      @RequestParam String lyDo,
      @RequestParam List<Integer> maBienThe,
      @RequestParam List<Integer> soLuong,
      @RequestParam(required = false) List<String> maBienTheThayThe,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    TaiKhoanLogin account = getCustomerAccount(session);
    if (account == null) {
      return session.getAttribute(ACCOUNT_ATTRIBUTE) == null ? "redirect:/dangnhap" : "redirect:/";
    }
    NhanVien customer = getCustomerDetails(account, session);
    if (customer == null) {
      return "redirect:/dangnhap";
    }
    if (maBienThe.size() != soLuong.size()
        || (maBienTheThayThe != null && maBienThe.size() != maBienTheThayThe.size())) {
      redirectAttributes.addFlashAttribute(
          "returnError", "Thông tin sản phẩm gửi lên không hợp lệ.");
      return "redirect:/don-hang/" + id + "/doi-tra";
    }
    try {
      LoaiYeuCauDoiTra requestType = LoaiYeuCauDoiTra.valueOf(loaiYeuCau);
      List<YeuCauDoiTraService.RequestLine> lines =
          java.util.stream.IntStream.range(0, maBienThe.size())
              .mapToObj(
                  index -> {
                    String replacementId =
                        maBienTheThayThe == null ? null : maBienTheThayThe.get(index);
                    Integer replacement =
                        replacementId == null || replacementId.isBlank()
                            ? null
                            : Integer.valueOf(replacementId);
                    return new YeuCauDoiTraService.RequestLine(
                        maBienThe.get(index), soLuong.get(index), replacement);
                  })
              .toList();
      yeuCauDoiTraService.createRequest(id, customer.getEmail(), requestType, lyDo, lines);
      redirectAttributes.addFlashAttribute(
          "orderResult", "Đã gửi yêu cầu đổi/trả. Cửa hàng sẽ xác nhận trong thời gian sớm nhất.");
      return "redirect:/don-hang/" + id;
    } catch (IllegalArgumentException exception) {
      redirectAttributes.addFlashAttribute("returnError", exception.getMessage());
      return "redirect:/don-hang/" + id + "/doi-tra";
    }
  }

  private Map<Integer, List<ChiTietSanPham>> getReplacementVariants(HoaDon order) {
    Map<Integer, List<ChiTietSanPham>> replacementVariants = new HashMap<>();
    if (order.getChiTietHoaDon() == null) {
      return replacementVariants;
    }
    for (ChiTietHoaDon line : order.getChiTietHoaDon()) {
      ChiTietSanPham original = line.getBienThe();
      if (original == null
          || original.getSanPham() == null
          || original.getSanPham().getMasanpham() == null) {
        continue;
      }
      int originalVariantId = original.getMachitietsanpham();
      int productId = original.getSanPham().getMasanpham();
      List<ChiTietSanPham> availableVariants =
          chiTietSanPhamService.getInforCTSanPhambyID(productId).stream()
              .filter(variant -> variant.getMachitietsanpham() != originalVariantId)
              .filter(variant -> variant.getSoluong() > 0)
              .filter(variant -> variant.getSanPham() != null)
              .filter(variant -> Boolean.FALSE.equals(variant.getSanPham().getHiden()))
              .toList();
      replacementVariants.put(originalVariantId, availableVariants);
    }
    return replacementVariants;
  }

  private NhanVien getCustomerDetails(TaiKhoanLogin account, HttpSession session) {
    NhanVien customer = nhanVienService.getInforbyId(account.getManhanvien());
    if (customer == null) {
      session.removeAttribute(ACCOUNT_ATTRIBUTE);
    }
    return customer;
  }

  private TaiKhoanLogin getCustomerAccount(HttpSession session) {
    Object value = session.getAttribute(ACCOUNT_ATTRIBUTE);
    if (value instanceof TaiKhoanLogin account && account.getMachucvu() == CUSTOMER_ROLE) {
      return account;
    }
    return null;
  }

  private boolean validateRegistration(DangKyModel dangKyUser, ModelMap modelMap) {
    String hoten = trimToEmpty(dangKyUser.getHotendk());
    String email = trimToEmpty(dangKyUser.getEmaildk());
    String diachi = trimToEmpty(dangKyUser.getDiachidk());
    String matkhau = trimToEmpty(dangKyUser.getMatkhaudk());
    String nhaplaiMk = trimToEmpty(dangKyUser.getNhaplaimkdk());
    String gioitinh = dangKyUser.getGioitinhdk();

    if (hoten.isEmpty()) {
      modelMap.addAttribute("error_hotendk", "Họ tên nhập không hợp lệ");
      return false;
    }
    if (email.isEmpty()) {
      modelMap.addAttribute("error_emaildk", "Nhập email không hợp lệ");
      return false;
    }
    if (diachi.isEmpty()) {
      modelMap.addAttribute("error_diachidk", "Địa chỉ không hợp lệ");
      return false;
    }
    if (matkhau.isEmpty()) {
      modelMap.addAttribute("error_matkhaudk", "Mật khẩu không hợp lệ");
      return false;
    }
    if (gioitinh == null || "null".equals(gioitinh)) {
      modelMap.addAttribute("error_gioitinhdk", "Bạn chưa chọn giới tính");
      return false;
    }
    if (nhaplaiMk.isEmpty()) {
      modelMap.addAttribute("error_nhaplaimatkhaudk", "Nhập lại mật khẩu không hợp lệ");
      return false;
    }
    if (!matkhau.equals(nhaplaiMk)) {
      modelMap.addAttribute("error_nhaplaimatkhaudk", "Nhập lại mật khẩu không đúng");
      return false;
    }
    if (nhanVienService.checkEmail(email)) {
      modelMap.addAttribute("error_emaildk", "Email trùng yêu cầu nhập email khác");
      return false;
    }
    return true;
  }

  private NhanVien buildCustomer(DangKyModel dangKyUser) {
    NhanVien nhanVien = new NhanVien();
    String email = trimToEmpty(dangKyUser.getEmaildk());

    nhanVien.setHoten(trimToEmpty(dangKyUser.getHotendk()));
    nhanVien.setDiachi(trimToEmpty(dangKyUser.getDiachidk()));
    nhanVien.setGioitinh(!"0".equals(dangKyUser.getGioitinhdk()));
    nhanVien.setEmail(email);
    nhanVien.setTendangnhap(email);
    nhanVien.setMatkhau(trimToEmpty(dangKyUser.getMatkhaudk()));
    nhanVien.setCmnd(null);
    nhanVien.setChucVu(chucVuService.getById(CUSTOMER_ROLE));
    nhanVien.setLock(false);
    nhanVien.setXacthuc(false);
    return nhanVien;
  }

  private TaiKhoanLogin buildLoginSession(NhanVien nhanVien) {
    TaiKhoanLogin taiKhoanLogin = new TaiKhoanLogin();
    taiKhoanLogin.setManhanvien(nhanVien.getManhanvien());
    taiKhoanLogin.setHoten(nhanVien.getHoten());
    taiKhoanLogin.setTendangnhap(nhanVien.getTendangnhap());
    taiKhoanLogin.setMachucvu(nhanVien.getChucVu().getMachucvu());
    return taiKhoanLogin;
  }

  private void sendVerificationEmail(String email, String token) throws MessagingException {
    MimeMessage message = mailsender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setTo(email);
    helper.setSubject("Yêu cầu xác thực email bạn đã đăng ký");
    String verificationUrl =
        UriComponentsBuilder.fromUriString(baseUrl)
            .path("/xac-thuc-email")
            .queryParam("token", token)
            .build()
            .toUriString();
    helper.setText(
        "Vui lòng nhấn vào liên kết sau để xác thực tài khoản: <a href=\""
            + verificationUrl
            + "\">Xác thực email</a>. Liên kết có hiệu lực trong 24 giờ.",
        true);
    mailsender.send(message);
  }

  private String generateVerificationToken() {
    byte[] tokenBytes = new byte[VERIFICATION_TOKEN_BYTES];
    SECURE_RANDOM.nextBytes(tokenBytes);
    return HexFormat.of().formatHex(tokenBytes);
  }

  private String trimToEmpty(String value) {
    return value == null ? "" : value.trim();
  }

  public int checkSecurityUser(HttpSession httpSession) {
    TaiKhoanLogin taiKhoanLogin = (TaiKhoanLogin) httpSession.getAttribute(ACCOUNT_ATTRIBUTE);
    return taiKhoanLogin == null ? NO_ROLE : taiKhoanLogin.getMachucvu();
  }
}

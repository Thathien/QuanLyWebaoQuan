package com.banhang.controller.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.ChucVu;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangKyModel;
import com.banhang.model.DangNhapModel;
import com.banhang.service.NhanVienService;

@Controller
@SessionAttributes({"taikhoan", "giohang"})
public class TaiKhoanUserController {
	
	@Autowired
	NhanVienService nhanVienService;
	
	@GetMapping("dangnhap")
	public String showdangnhap(Model m,HttpSession httpSession) {
		int  temp=checkSecurityUser(httpSession);
		if(temp==0) {
			m.addAttribute("dangnhapuser",new DangNhapModel());
			return "login_user";
		}else if(temp==1) {
			return "redirect:/admin";
		}else {
			return "redirect:/";
		}
	}
	
	@PostMapping("dangnhap")
	public String dangnhapProcess(@Valid @ModelAttribute("dangnhapuser") DangNhapModel dangnhapuser,BindingResult br,ModelMap map) {
		if(br.hasErrors()) {
			return "login_user";
		}else {
			boolean temp=nhanVienService.checkUser(dangnhapuser.getEmail(),dangnhapuser.getMatKhau());
			if(temp==true) {
				NhanVien tk= new NhanVien();
				tk=nhanVienService.getInforby_User_Pass(dangnhapuser.getEmail(), dangnhapuser.getMatKhau());
				TaiKhoanLogin taikhoan_session= new TaiKhoanLogin();
				taikhoan_session.setManhanvien(tk.getmanhanvien());
				taikhoan_session.setHoten(tk.getHoten());
				taikhoan_session.setTendangnhap(tk.getTendangnhap());
				taikhoan_session.setMatkhau(tk.getMatkhau());
				taikhoan_session.setMachucvu(tk.getChucVu().getMachucvu());
				map.addAttribute("taikhoan",taikhoan_session);
//				httpSession.setAttribute("taikhoan", taikhoan_session););
				return "redirect:/";
			}else {
				map.addAttribute("resultDangNhap","Tài khoản hoặc mật khẩu không hơp lệ");
				return "login_user";
			}
		}
		
	}
	
	@GetMapping("logout")
	public String logout(HttpSession httpSession, Model model) {
		httpSession.removeAttribute("taikhoan");
		httpSession.invalidate();
		if(model.containsAttribute("taikhoan")) {
			model.asMap().remove("taikhoan");
		}
		return "redirect:/";
	}
	
	@GetMapping("dangky")
	public String showdangky(Model m) {
		m.addAttribute("dangkyuser",new DangKyModel());
		return "regester_user";
	}
	@PostMapping("dangky")
	public String dangkyProcess(@Valid @ModelAttribute("dangkyuser") DangKyModel dangkyuser,BindingResult br,ModelMap map) {
		if(br.hasErrors()) {
			return "regester_user";
		}else {
			if(dangkyuser.getHotendk().trim()!=null) {
				if(dangkyuser.getEmaildk().trim()!=null){
					if(dangkyuser.getDiachidk().trim()!=null) {
						if(dangkyuser.getMatkhaudk().trim()!=null) {
							if(!dangkyuser.getGioitinhdk().equals("null")) {
								if(dangkyuser.getNhaplaimkdk().trim()!=null) {
									if(dangkyuser.getMatkhaudk().trim().equals(dangkyuser.getNhaplaimkdk().trim())==true) {
										if(nhanVienService.checkEmail(dangkyuser.getEmaildk())==false) {
											NhanVien nv= new NhanVien();
											nv.setHoten(dangkyuser.getHotendk().trim());
											nv.setDiachi(dangkyuser.getDiachidk().trim());
											// 0-> nữ;1-> nam
											if(Integer.parseInt(dangkyuser.getGioitinhdk())==0){
												nv.setGioitinh(false);
											}else {
												nv.setGioitinh(true);
											}
											nv.setEmail(dangkyuser.getEmaildk().trim());
											nv.setTendangnhap(dangkyuser.getEmaildk().trim());
											nv.setMatkhau(dangkyuser.getMatkhaudk().trim());
											nv.setCmnd(null);
											nv.setChucVu(new ChucVu(3,"nguoidung"));
											nv.setLock(false);
											nv.setXacthuc(false);
											int id=nhanVienService.register(nv);
											if(id!=0) {
												map.addAttribute("success_fail", "Đăng ký thành công hãy truy cập email của bạn để xác thực");
											}else {
												map.addAttribute("success_fail", "Đăng ký không thành công");
											}
										}else {
											map.addAttribute("error_emaildk", "Email trùng yêu cầu nhập email khác");
										}
									}else {
										map.addAttribute("error_nhaplaimatkhaudk", "Nhập lại mật khẩu không đúng");
									}
								}else {
									map.addAttribute("error_nhaplaimatkhaudk", "Nhập lại mật khẩu không hợp lệ");
								}
							}else {
								map.addAttribute("error_gioitinhdk","Bạn chưa chọn giới tính");
							}
						}else {
							map.addAttribute("error_matkhaudk", "Mật khẩu không hợp lệ");
						}
					}else {
						map.addAttribute("error_diachidk", "Địa chỉ không hợp lệ");
					}
				}else {
					map.addAttribute("error_emaildk","Nhập email không hợp lệ");
				}
			}else {
				map.addAttribute("error_hotendk", "Họ tên nhập không hợp lệ");
			}
			return "regester_user";
		}
		
	}
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
		return matcher.find();
	}
	//admin -> 1  // other  -> 0
	public int checkSecurityUser(HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
			if(taiKhoanLogin!=null) {
				return taiKhoanLogin.getMachucvu();
			}
		return 0;
	}
}

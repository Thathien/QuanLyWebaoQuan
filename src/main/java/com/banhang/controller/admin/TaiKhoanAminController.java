package com.banhang.controller.admin;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangNhapModel;
import com.banhang.service.NhanVienService;

@Controller
@SessionAttributes({"taikhoan"})
@RequestMapping("/admin")
public class TaiKhoanAminController {
	
	@Autowired
	NhanVienService nhanVienService;
	
	@GetMapping("dangnhap")
	public String showDangNhap(Model m,HttpSession httpSession) {
		int temp=checkSecurityUser(httpSession);
		
		// 1 -> admin  !! other exepted 0 -> user  !! 0 no login
		if(temp==1){
			return "redirect:/admin";
		}else if(temp!=1 && temp!=0){
			return "redirect:/";
		}else {
			m.addAttribute("dangnhapadmin", new DangNhapModel());
			return "login_admin";
		}
	}
	
	@PostMapping("dangnhap")
	public String dangNhapProcess(@Valid @ModelAttribute("dangnhapadmin") DangNhapModel dangnhapadmin,BindingResult br,ModelMap map) {
		if(br.hasErrors()) {
			return "login_admin";
		}else {
			boolean temp= nhanVienService.checkAdmin(dangnhapadmin.getEmail(), dangnhapadmin.getMatKhau());
			if(temp==true) {
				NhanVien tk= new NhanVien();
				tk=nhanVienService.getInforby_User_Pass(dangnhapadmin.getEmail(), dangnhapadmin.getMatKhau());
				TaiKhoanLogin taikhoan= new TaiKhoanLogin();
				taikhoan.setHoten(tk.getHoten());
				taikhoan.setMachucvu(tk.getChucVu().getMachucvu());
				taikhoan.setManhanvien(tk.getmanhanvien());
				taikhoan.setTendangnhap(tk.getEmail());
				taikhoan.setMatkhau(tk.getMatkhau());
//				httpSession.setAttribute("taikhoan", taikhoan);
				map.addAttribute("taikhoan", taikhoan);
				return "redirect:/admin";
			}else {
				map.addAttribute("resultsAdmin", "Bạn không đủ quyền để truy cập");
				return "login_admin";
			}
		}
	}
	@GetMapping("dangxuat")
	public String logout(HttpSession httpSession, Model model) {
		httpSession.removeAttribute("taikhoan");
		httpSession.invalidate();
		if(model.containsAttribute("taikhoan")) {
			model.asMap().remove("taikhoan");
		}
		return "redirect:/admin/dangnhap";
	}
	
	//admin -> tru  // other  -> false
		public boolean checkSecurity(HttpSession httpSession) {
			TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
			if(taiKhoanLogin!=null) {
				if(taiKhoanLogin.getMachucvu()==1) {
					return true;
				}
			}
			return false;
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

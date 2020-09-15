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

import com.banhang.commons.TaiKhoanLogin;
import com.banhang.entity.NhanVien;
import com.banhang.model.DangNhapModel;
import com.banhang.service.NhanVienService;

@Controller
public class TaiKhoanAminController {
	
	@Autowired
	NhanVienService nhanVienService;
	
	@GetMapping("/admin/dangnhap")
	public String showDangNhap(Model m) {
		m.addAttribute("dangnhapadmin", new DangNhapModel());
		return "login_admin";
	}
	
	@PostMapping("/admin/dangnhap")
	public String dangNhapProcess(@Valid @ModelAttribute("dangnhapadmin") DangNhapModel dangnhapadmin,BindingResult br,ModelMap map,HttpSession httpSession) {
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
				httpSession.setAttribute("taikhoan", taikhoan);
				return "/banaoquan/admin/";
			}else {
				map.addAttribute("resultsAdmin", "Bạn không đủ quyền để truy cập");
				return "login_admin";
			}
		}
	}
	
	
}

package com.banhang.controller.admin;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.banhang.model.DangNhapModel;

@Controller
public class TaiKhoanAminController {
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
			
		}
		return null;
	}
	
	
}

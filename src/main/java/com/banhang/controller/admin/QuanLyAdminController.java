package com.banhang.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;

@Controller
@SessionAttributes({"taikhoan"})
public class QuanLyAdminController {

	@GetMapping("admin/product")
	public String index(ModelMap modelMap,HttpSession httpSession) {
		TaiKhoanLogin taiKhoanLogin=(TaiKhoanLogin) httpSession.getAttribute("taikhoan");
		if(taiKhoanLogin!=null) {
			if(taiKhoanLogin.getMachucvu()==1) {
				return "MG_product_admin";
			}else {
				return "401";
			}
		}
		return "login_admin";
	}
}

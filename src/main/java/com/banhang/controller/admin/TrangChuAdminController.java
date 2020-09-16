package com.banhang.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.banhang.commons.TaiKhoanLogin;

@Controller
@SessionAttributes({"taikhoan"})
public class TrangChuAdminController {
	@GetMapping("admin")
	public String index(ModelMap map, HttpSession httpSession) {
		boolean checkS=checkSecurity(httpSession);
		if(checkS==true) {
			
			return "index_admin";
		}
		return "401";
		
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
}

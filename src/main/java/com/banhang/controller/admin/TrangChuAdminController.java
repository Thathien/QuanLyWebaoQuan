package com.banhang.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrangChuAdminController {
	@GetMapping("admin")
	public String index() {
		return "index_admin";
	}
}

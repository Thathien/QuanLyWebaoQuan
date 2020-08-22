package com.banhang.model;

import javax.validation.constraints.Size;

import org.apache.commons.io.FileUtils;

public class DangNhapModel {
	 
	@Size(min = 8, message = "Yêu cầu nhập tài khoản")
	private String email;
	
	@Size(min = 2, message = "Mật khẩu ít nhất 6 ký tự")
	private String matKhau;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	
	
	
}

package com.banhang.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

public class DangKyModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(max = 40,min = 5,message = "Yêu cầu nhập lại họ tên")
	private String hotendk;
	
	@Size(min = 10,message = "Email không hợp lệ")
	private String emaildk;
	
	@Size(min = 6,message = "Mật khẩu không hợp lệ")
	private String matkhaudk;
	
	private String nhaplaimkdk;
	
	private String gioitinhdk;
	
	@Size(min = 6,message = "Địa chỉ không hợp lệ")
	private String diachidk;

	public DangKyModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHotendk() {
		return hotendk;
	}

	public void setHotendk(String hotendk) {
		this.hotendk = hotendk;
	}

	public String getEmaildk() {
		return emaildk;
	}

	public void setEmaildk(String emaildk) {
		this.emaildk = emaildk;
	}

	public String getMatkhaudk() {
		return matkhaudk;
	}

	public void setMatkhaudk(String matkhaudk) {
		this.matkhaudk = matkhaudk;
	}

	public String getNhaplaimkdk() {
		return nhaplaimkdk;
	}

	public void setNhaplaimkdk(String nhaplaimkdk) {
		this.nhaplaimkdk = nhaplaimkdk;
	}

	public String getGioitinhdk() {
		return gioitinhdk;
	}

	public void setGioitinhdk(String gioitinhdk) {
		this.gioitinhdk = gioitinhdk;
	}

	public String getDiachidk() {
		return diachidk;
	}

	public void setDiachidk(String diachidk) {
		this.diachidk = diachidk;
	}

	
}

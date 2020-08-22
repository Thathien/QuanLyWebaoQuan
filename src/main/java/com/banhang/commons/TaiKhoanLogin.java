package com.banhang.commons;

public class TaiKhoanLogin {
	
	private int manhanvien;
	
	private String tendangnhap;
	
	private String matkhau;
	 
	private String hoten;
	
	private int machucvu;
	
	public TaiKhoanLogin() {
		super();
	}

	public TaiKhoanLogin(int manhanvien, String tendangnhap, String matkhau, String hoten, int machucvu) {
		super();
		this.manhanvien = manhanvien;
		this.tendangnhap = tendangnhap;
		this.matkhau = matkhau;
		this.hoten = hoten;
		this.machucvu = machucvu;
	}

	public int getManhanvien() {
		return manhanvien;
	}

	public void setManhanvien(int manhanvien) {
		this.manhanvien = manhanvien;
	}

	public String getTendangnhap() {
		return tendangnhap;
	}

	public void setTendangnhap(String tendangnhap) {
		this.tendangnhap = tendangnhap;
	}

	public String getMatkhau() {
		return matkhau;
	}

	public void setMatkhau(String matkhau) {
		this.matkhau = matkhau;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public int getMachucvu() {
		return machucvu;
	}

	public void setMachucvu(int machucvu) {
		this.machucvu = machucvu;
	}

	
}

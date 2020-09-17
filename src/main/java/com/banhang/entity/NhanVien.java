package com.banhang.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;



@Entity(name = "NHANVIEN")
public class NhanVien {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int manhanvien;
	String hoten;
	String diachi;
	boolean gioitinh;
	String cmnd;
	boolean lock;
	String lydokhoa;
	boolean xacthuc;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "machucvu")
	ChucVu chucVu;
	
	String email;
	String tendangnhap;
	String matkhau;
	
	public NhanVien() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public NhanVien(int manhanvien, String hoten, String diachi, boolean gioitinh, String cmnd, boolean lock,
			String lydokhoa, boolean xacthuc, ChucVu chucVu, String email, String tendangnhap, String matkhau) {
		super();
		this.manhanvien = manhanvien;
		this.hoten = hoten;
		this.diachi = diachi;
		this.gioitinh = gioitinh;
		this.cmnd = cmnd;
		this.lock = lock;
		this.lydokhoa = lydokhoa;
		this.xacthuc = xacthuc;
		this.chucVu = chucVu;
		this.email = email;
		this.tendangnhap = tendangnhap;
		this.matkhau = matkhau;
	}



	public int getmanhanvien() {
		return manhanvien;
	}
	public void setmanhanvien(int manhanvien) {
		this.manhanvien = manhanvien;
	}
	public String getHoten() {
		return hoten;
	}
	public void setHoten(String hoten) {
		this.hoten = hoten;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	public boolean isGioitinh() {
		return gioitinh;
	}
	public void setGioitinh(boolean gioitinh) {
		this.gioitinh = gioitinh;
	}
	public String getCmnd() {
		return cmnd;
	}
	public void setCmnd(String cmnd) {
		this.cmnd = cmnd;
	}
	public ChucVu getChucVu() {
		return chucVu;
	}
	public void setChucVu(ChucVu chucVu) {
		this.chucVu = chucVu;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public int getManhanvien() {
		return manhanvien;
	}
	public void setManhanvien(int manhanvien) {
		this.manhanvien = manhanvien;
	}



	public boolean isLock() {
		return lock;
	}
	



	public void setLock(boolean lock) {
		this.lock = lock;
	}



	public String getLydokhoa() {
		return lydokhoa;
	}



	public void setLydokhoa(String lydokhoa) {
		this.lydokhoa = lydokhoa;
	}



	public boolean isXacthuc() {
		return xacthuc;
	}



	public void setXacthuc(boolean xacthuc) {
		this.xacthuc = xacthuc;
	}
	
	
}

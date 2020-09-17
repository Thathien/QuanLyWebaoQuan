package com.banhang.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "CHITIETSANPHAM")
public class ChiTietSanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int machitietsanpham;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "masanpham")
	SanPham sanPham;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "masize")
	SizeSanPham sizeSanPham;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mamau")
	MauSanPham mauSanPham;
	
	int soluong;
	String ngaynhap;
	public ChiTietSanPham(int machitietsanpham, SanPham sanPham, SizeSanPham sizeSanPham, MauSanPham mauSanPham,
			int soluong, String ngaynhap) {
		super();
		this.machitietsanpham = machitietsanpham;
		this.sanPham = sanPham;
		this.sizeSanPham = sizeSanPham;
		this.mauSanPham = mauSanPham;
		this.soluong = soluong;
		this.ngaynhap = ngaynhap;
	}
	public ChiTietSanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getMachitietsanpham() {
		return machitietsanpham;
	}
	public void setMachitietsanpham(int machitietsanpham) {
		this.machitietsanpham = machitietsanpham;
	}
	public SanPham getSanPham() {
		return sanPham;
	}
	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}
	public SizeSanPham getSizeSanPham() {
		return sizeSanPham;
	}
	public void setSizeSanPham(SizeSanPham sizeSanPham) {
		this.sizeSanPham = sizeSanPham;
	}
	public MauSanPham getMauSanPham() {
		return mauSanPham;
	}
	public void setMauSanPham(MauSanPham mauSanPham) {
		this.mauSanPham = mauSanPham;
	}
	public int getSoluong() {
		return soluong;
	}
	public void setSoluong(int soluong) {
		this.soluong = soluong;
	}
	public String getNgaynhap() {
		return ngaynhap;
	}
	public void setNgaynhap(String ngaynhap) {
		this.ngaynhap = ngaynhap;
	}
	
	
	
	
}

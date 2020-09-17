package com.banhang.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name = "SANPHAM")
public class SanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int masanpham;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "madanhmucsanpham")
	DanhMucSanPham danhMucSanPham;
	
	String tensanpham;
	String giatien;
	String mota;
	String hinhsanpham;
	boolean hiden;
	String doituong;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "masanpham")
	Set<ChiTietSanPham> chiTietSanPham;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "CHITIETKHUYENMAI",
	joinColumns ={@JoinColumn(name = "masanpham",referencedColumnName ="masanpham")},
	inverseJoinColumns = {@JoinColumn(name = "makhuyenmai",referencedColumnName ="makhuyenmai")})
	Set<KhuyenMai> khuyenMai;
	
	public SanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	


	public SanPham(int masanpham, DanhMucSanPham danhMucSanPham, String tensanpham, String giatien, String mota,
			String hinhsanpham, boolean hiden, String doituong, Set<ChiTietSanPham> chiTietSanPham,
			Set<KhuyenMai> khuyenMai) {
		super();
		this.masanpham = masanpham;
		this.danhMucSanPham = danhMucSanPham;
		this.tensanpham = tensanpham;
		this.giatien = giatien;
		this.mota = mota;
		this.hinhsanpham = hinhsanpham;
		this.hiden = hiden;
		this.doituong = doituong;
		this.chiTietSanPham = chiTietSanPham;
		this.khuyenMai = khuyenMai;
	}






	public int getMasanpham() {
		return masanpham;
	}
	public void setMasanpham(int masanpham) {
		this.masanpham = masanpham;
	}
	public DanhMucSanPham getDanhMucSanPham() {
		return danhMucSanPham;
	}
	public void setDanhMucSanPham(DanhMucSanPham danhMucSanPham) {
		this.danhMucSanPham = danhMucSanPham;
	}
	public String getTensanpham() {
		return tensanpham;
	}
	public void setTensanpham(String tensanpham) {
		this.tensanpham = tensanpham;
	}
	public String getGiatien() {
		return giatien;
	}
	public void setGiatien(String giatien) {
		this.giatien = giatien;
	}
	public String getMota() {
		return mota;
	}
	public void setMota(String mota) {
		this.mota = mota;
	}
	public String getHinhsanpham() {
		return hinhsanpham;
	}
	public void setHinhsanpham(String hinhsanpham) {
		this.hinhsanpham = hinhsanpham;
	}
	public Set<ChiTietSanPham> getChiTietSanPham() {
		return chiTietSanPham;
	}
	public void setChiTietSanPham(Set<ChiTietSanPham> chiTietSanPham) {
		this.chiTietSanPham = chiTietSanPham;
	}
	public Set<KhuyenMai> getKhuyenMai() {
		return khuyenMai;
	}
	public void setKhuyenMai(Set<KhuyenMai> khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public boolean isHiden() {
		return hiden;
	}

	public void setHiden(boolean hiden) {
		this.hiden = hiden;
	}






	public String getDoituong() {
		return doituong;
	}






	public void setDoituong(String doituong) {
		this.doituong = doituong;
	}
	
	
}

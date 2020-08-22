package com.banhang.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "DANHMUCSANPHAM")
public class DanhMucSanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int madanhmucsanpham;
	String tendanhmuc;
	String hinhdanhmuc;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "madanhmucsanpham")
	Set<SanPham>danhsachsanpham;
	public DanhMucSanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DanhMucSanPham(int madanhmucsanpham, String tendanhmuc, String hinhdanhmuc, Set<SanPham> danhsachsanpham) {
		super();
		this.madanhmucsanpham = madanhmucsanpham;
		this.tendanhmuc = tendanhmuc;
		this.hinhdanhmuc = hinhdanhmuc;
		this.danhsachsanpham = danhsachsanpham;
	}
	public int getMadanhmucsanpham() {
		return madanhmucsanpham;
	}
	public void setMadanhmucsanpham(int madanhmucsanpham) {
		this.madanhmucsanpham = madanhmucsanpham;
	}
	public String getTendanhmuc() {
		return tendanhmuc;
	}
	public void setTendanhmuc(String tendanhmuc) {
		this.tendanhmuc = tendanhmuc;
	}
	public String getHinhdanhmuc() {
		return hinhdanhmuc;
	}
	public void setHinhdanhmuc(String hinhdanhmuc) {
		this.hinhdanhmuc = hinhdanhmuc;
	}
	public Set<SanPham> getDanhsachsanpham() {
		return danhsachsanpham;
	}
	public void setDanhsachsanpham(Set<SanPham> danhsachsanpham) {
		this.danhsachsanpham = danhsachsanpham;
	}
	

	
}

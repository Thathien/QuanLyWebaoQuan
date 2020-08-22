package com.banhang.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity(name = "KHUYENMAI")
public class KhuyenMai {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int makhuyenmai;
	float giagiam;
	String tenkhuyenmai;
	String thoigianbatdau;
	String thoigianketthuc;
	String mota;
	String hinhkhuyenmai;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "CHITIETKHUYENMAI",
	joinColumns ={@JoinColumn(name = "makhuyenmai",referencedColumnName ="makhuyenmai")},
	inverseJoinColumns = {@JoinColumn(name = "masanpham",referencedColumnName ="masanpham")})
	Set<SanPham> sanPham;

	public KhuyenMai() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhuyenMai(int makhuyenmai, float giagiam, String tenkhuyenmai, String thoigianbatdau, String thoigianketthuc,
			String mota, String hinhkhuyenmai, Set<SanPham> sanPham) {
		super();
		this.makhuyenmai = makhuyenmai;
		this.giagiam = giagiam;
		this.tenkhuyenmai = tenkhuyenmai;
		this.thoigianbatdau = thoigianbatdau;
		this.thoigianketthuc = thoigianketthuc;
		this.mota = mota;
		this.hinhkhuyenmai = hinhkhuyenmai;
		this.sanPham = sanPham;
	}
	
	public int getMakhuyenmai() {
		return makhuyenmai;
	}

	public void setMakhuyenmai(int makhuyenmai) {
		this.makhuyenmai = makhuyenmai;
	}

	public float getGiagiam() {
		return giagiam;
	}

	public void setGiagiam(float giagiam) {
		this.giagiam = giagiam;
	}

	public String getTenkhuyenmai() {
		return tenkhuyenmai;
	}

	public void setTenkhuyenmai(String tenkhuyenmai) {
		this.tenkhuyenmai = tenkhuyenmai;
	}

	public String getThoigianbatdau() {
		return thoigianbatdau;
	}

	public void setThoigianbatdau(String thoigianbatdau) {
		this.thoigianbatdau = thoigianbatdau;
	}

	public String getThoigianketthuc() {
		return thoigianketthuc;
	}

	public void setThoigianketthuc(String thoigianketthuc) {
		this.thoigianketthuc = thoigianketthuc;
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

	public String getHinhkhuyenmai() {
		return hinhkhuyenmai;
	}

	public void setHinhkhuyenmai(String hinhkhuyenmai) {
		this.hinhkhuyenmai = hinhkhuyenmai;
	}

	public Set<SanPham> getSanPham() {
		return sanPham;
	}

	public void setSanPham(Set<SanPham> sanPham) {
		this.sanPham = sanPham;
	}
	
	
}

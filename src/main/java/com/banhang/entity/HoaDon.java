package com.banhang.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "HOADON")
@Table(name = "HOADON")
public class HoaDon implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mahoadon")
	int mahoadon;
	@Column(name = "tenkhachhang",columnDefinition = "nvarchar(50)")
	String tenkhachhang;
	@Column(name = "sdt",columnDefinition = "nvarchar(50)")
	String sdt;
	String diachigiaohang;
	@Column(name = "tinhtrang",columnDefinition = "bit")
	int tinhtrang;
	@Column(name = "ghichu",columnDefinition = "nvarchar(50)")
	String ghichu;
	@Column(name = "ngaylap",columnDefinition = "nvarchar(50)")
	String ngaylap;
	@Column(name = "hinhthucgiaohang",columnDefinition = "nvarchar(50)")
	String hinhthucgiaohang;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "mahoadon")
	Set<ChiTietHoaDon> chiTietHoaDon;

	public HoaDon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HoaDon(int mahoadon, String tenkhachhang, String sdt, String diachigiaohang, int tinhtrang, String ghichu,
			String ngaylap, String hinhthucgiaohang, Set<ChiTietHoaDon> chiTietHoaDon) {
		super();
		this.mahoadon = mahoadon;
		this.tenkhachhang = tenkhachhang;
		this.sdt = sdt;
		this.diachigiaohang = diachigiaohang;
		this.tinhtrang = tinhtrang;
		this.ghichu = ghichu;
		this.ngaylap = ngaylap;
		this.hinhthucgiaohang = hinhthucgiaohang;
		this.chiTietHoaDon = chiTietHoaDon;
	}

	public int getMahoadon() {
		return mahoadon;
	}

	public void setMahoadon(int mahoadon) {
		this.mahoadon = mahoadon;
	}

	public String getTenkhachhang() {
		return tenkhachhang;
	}

	public void setTenkhachhang(String tenkhachhang) {
		this.tenkhachhang = tenkhachhang;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getDiachigiaohang() {
		return diachigiaohang;
	}

	public void setDiachigiaohang(String diachigiaohang) {
		this.diachigiaohang = diachigiaohang;
	}

	public int getTinhtrang() {
		return tinhtrang;
	}

	public void setTinhtrang(int tinhtrang) {
		this.tinhtrang = tinhtrang;
	}

	public String getNgaylap() {
		return ngaylap;
	}

	public void setNgaylap(String ngaylap) {
		this.ngaylap = ngaylap;
	}

	public Set<ChiTietHoaDon> getChiTietHoaDon() {
		return chiTietHoaDon;
	}

	public void setChiTietHoaDon(Set<ChiTietHoaDon> chiTietHoaDon) {
		this.chiTietHoaDon = chiTietHoaDon;
	}

	public String getGhichu() {
		return ghichu;
	}

	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}

	public String getHinhthucgiaohang() {
		return hinhthucgiaohang;
	}

	public void setHinhthucgiaohang(String hinhthucgiaohang) {
		this.hinhthucgiaohang = hinhthucgiaohang;
	}
	
	
}

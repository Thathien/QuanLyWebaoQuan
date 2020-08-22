package com.banhang.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "HOADON")
public class HoaDon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int mahoadon;
	String tenkhachhang;
	String sdt;
	String diachigiaohang;
	int tinhtrang;
	String ngaylap;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "mahoadon")
	Set<ChiTietHoaDon> chiTietHoaDon;

	public HoaDon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HoaDon(int mahoadon, String tenkhachhang, String sdt, String diachigiaohang, int tinhtrang, String ngaylap,
			Set<ChiTietHoaDon> chiTietHoaDon) {
		super();
		this.mahoadon = mahoadon;
		this.tenkhachhang = tenkhachhang;
		this.sdt = sdt;
		this.diachigiaohang = diachigiaohang;
		this.tinhtrang = tinhtrang;
		this.ngaylap = ngaylap;
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
	
}

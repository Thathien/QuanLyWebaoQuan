package com.banhang.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


@Entity(name = "CHITIETHOADON")
public class ChiTietHoaDon {
	@EmbeddedId
	ChiTietHoaDonId chiTietHoaDonId;
	int soluong;
	String giatien;
	public ChiTietHoaDon() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChiTietHoaDon(ChiTietHoaDonId chiTietHoaDonId, int soluong, String giatien) {
		super();
		this.chiTietHoaDonId = chiTietHoaDonId;
		this.soluong = soluong;
		this.giatien = giatien;
	}
	public ChiTietHoaDonId getChiTietHoaDonId() {
		return chiTietHoaDonId;
	}
	public void setChiTietHoaDonId(ChiTietHoaDonId chiTietHoaDonId) {
		this.chiTietHoaDonId = chiTietHoaDonId;
	}
	public int getSoluong() {
		return soluong;
	}
	public void setSoluong(int soluong) {
		this.soluong = soluong;
	}
	public String getGiatien() {
		return giatien;
	}
	public void setGiatien(String giatien) {
		this.giatien = giatien;
	}
	
}

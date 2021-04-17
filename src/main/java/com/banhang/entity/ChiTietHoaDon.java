package com.banhang.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity(name = "CHITIETHOADON")
@Table(name = "CHITIETHOADON")
public class ChiTietHoaDon implements Serializable{
	@EmbeddedId
	ChiTietHoaDonId chiTietHoaDonId;
	@Column(name = "soluong",columnDefinition = "int")
	private int soluong;
	@Column(name = "giatien" ,columnDefinition = "nvarchar(50)")
	private String giatien;
	
	@ManyToOne
	@JoinColumn(name = "mahoadon",insertable = false,updatable = false)
	private HoaDon hoaDon;
//	public ChiTietHoaDon() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	public ChiTietHoaDon(ChiTietHoaDonId chiTietHoaDonId, int soluong, String giatien) {
//		super();
//		this.chiTietHoaDonId = chiTietHoaDonId;
//		this.soluong = soluong;
//		this.giatien = giatien;
//	}
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

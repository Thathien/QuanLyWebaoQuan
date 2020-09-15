package com.banhang.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ChiTietHoaDonId implements Serializable{
	int mahoadon;
	int machitietsanpham;
//	public ChiTietHoaDonId() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	public ChiTietHoaDonId(int mahoadon, int machitietsanpham) {
//		super();
//		this.mahoadon = mahoadon;
//		this.machitietsanpham = machitietsanpham;
//	}
	public int getMahoadon() {
		return mahoadon;
	}
	public void setMahoadon(int mahoadon) {
		this.mahoadon = mahoadon;
	}
	public int getMachitietsanpham() {
		return machitietsanpham;
	}
	public void setMachitietsanpham(int machitietsanpham) {
		this.machitietsanpham = machitietsanpham;
	}
	
}

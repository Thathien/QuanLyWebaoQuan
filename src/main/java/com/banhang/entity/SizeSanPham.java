package com.banhang.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "SIZESANPHAM")
public class SizeSanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int masize;
	String size;
	public SizeSanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SizeSanPham(int masize, String size) {
		super();
		this.masize = masize;
		this.size = size;
	}
	public int getMasize() {
		return masize;
	}
	public void setMasize(int masize) {
		this.masize = masize;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
}

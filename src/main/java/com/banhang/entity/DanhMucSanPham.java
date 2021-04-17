package com.banhang.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "DANHMUCSANPHAM")
@Table(name = "DANHMUCSANPHAM")
public class DanhMucSanPham implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "madanhmucsanpham")
	int madanhmucsanpham;
	@Column(name = "tendanhmuc", columnDefinition = "nvarchar(50)")
	String tendanhmuc;
	@Column(name = "hinhdanhmuc", columnDefinition = "nvarchar(50)")
	String hinhdanhmuc;
	@Column(name = "hiden" , columnDefinition = "bit")
	boolean hiden;
//	cascade = CascadeType.ALL MEGER
	@OneToMany( cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "madanhmucsanpham")
	Set<SanPham>danhsachsanpham;
	public DanhMucSanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DanhMucSanPham(int madanhmucsanpham, String tendanhmuc, String hinhdanhmuc, boolean hiden,
			Set<SanPham> danhsachsanpham) {
		super();
		this.madanhmucsanpham = madanhmucsanpham;
		this.tendanhmuc = tendanhmuc;
		this.hinhdanhmuc = hinhdanhmuc;
		this.hiden = hiden;
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

	public boolean isHiden() {
		return hiden;
	}

	public void setHiden(boolean hiden) {
		this.hiden = hiden;
	}
	

	
}

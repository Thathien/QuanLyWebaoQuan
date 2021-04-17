package com.banhang.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "SANPHAM")
@Table(name = "SANPHAM")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPham implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "masanpham")
	int masanpham;
//	,fetch = FetchType.EAGER
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "madanhmucsanpham")
	DanhMucSanPham danhMucSanPham;
	
	
	@Column(name = "tensanpham" , columnDefinition = "nvarchar(40)")
	String tensanpham;
	
	@Column(name = "giatien" , columnDefinition = "nvarchar(40)")
	String giatien;
	
	@Column(name = "mota" , columnDefinition = "nvarchar(40)")
	String mota;
	
	@Column(name = "hinhsanpham" , columnDefinition = "nvarchar(40)")
	String hinhsanpham;
	
	@Column(name = "hiden", columnDefinition = "bit")
	boolean hiden;
	
	@Column(name = "doituong" , columnDefinition = "nvarchar(40)")
	String doituong;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "sanPham")
	Set<ChiTietSanPham> chiTietSanPham= new HashSet<ChiTietSanPham>();
	
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "CHITIETKHUYENMAI",
		joinColumns ={@JoinColumn(name = "masanpham",referencedColumnName ="masanpham")},
		inverseJoinColumns = {@JoinColumn(name = "makhuyenmai",referencedColumnName ="makhuyenmai")})
	Set<KhuyenMai> khuyenMai= new HashSet<KhuyenMai>();
	

}

package com.banhang.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity(name = "CHITIETSANPHAM")
@Table(name ="CHITIETSANPHAM" )
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChiTietSanPham implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "machitietsanpham")
	int machitietsanpham;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "masanpham")
	SanPham sanPham;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "masize")
	SizeSanPham sizeSanPham;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mamau")
	MauSanPham mauSanPham;
	
	int soluong;
	
	
	String ngaynhap;

}

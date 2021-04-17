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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "NHANVIEN")
@Table(name = "NHANVIEN")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class NhanVien implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "manhanvien")
	int manhanvien;
	@Column(name = "hoten",columnDefinition = "nvarchar(40)")
	String hoten;
	@Column(name = "diachi",columnDefinition = "nvarchar(40)")
	String diachi;
	@Column(name = "gioitinh", columnDefinition = "bit")
	boolean gioitinh;
	@Column(name = "cmnd",columnDefinition = "nvarchar(40)")
	String cmnd;
	@Column(name = "lock", columnDefinition = "bit")
	boolean lock;
	@Column(name = "lydokhoa",columnDefinition = "nvarchar(40)")
	String lydokhoa;
	@Column(name = "xacthuc", columnDefinition = "bit")
	boolean xacthuc;
	
	@ManyToOne 
	@JoinColumn(name = "machucvu")
	ChucVu chucVu;
	String email;
	String tendangnhap;
	String matkhau;
	
	
}

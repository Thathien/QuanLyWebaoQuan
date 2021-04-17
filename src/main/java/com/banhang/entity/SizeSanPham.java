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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "SIZESANPHAM")
@Table(name = "SIZESANPHAM")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SizeSanPham implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "masize")
	int masize;
	
	@Column(name = "size" , columnDefinition = "nvarchar(50)")
	String size;
	
//	@OneToOne(mappedBy = "sizeSanPham")
	@OneToMany(mappedBy = "sizeSanPham",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//	private ChiTietSanPham chiTietSanPham;
	private Set<ChiTietSanPham> chiTietSanPhams= new HashSet<ChiTietSanPham>();

	
	
}

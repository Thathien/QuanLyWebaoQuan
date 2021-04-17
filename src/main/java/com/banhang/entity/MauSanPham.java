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

@Entity(name = "MAUSANPHAM")
@Table(name = "MAUSANPHAM")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MauSanPham implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mamau")
	int mamau;
	@Column(name = "tenmau", columnDefinition = "nvarchar(50)")
	String tenmau;
	@OneToMany(mappedBy = "mauSanPham",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Set<ChiTietSanPham> chiTietSanPhams= new HashSet<>();

	
}

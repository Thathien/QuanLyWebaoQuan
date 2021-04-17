package com.banhang.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity(name = "CHUCVU")
@Table(name = "CHUCVU")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChucVu implements Serializable{
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "machucvu")
	int machucvu;
	@Column(name = "tenchucvu")
	String tenchucvu;
	
	@OneToMany(mappedBy ="chucVu" )
	private Set<NhanVien> nhanViens= new HashSet<NhanVien>();

	public ChucVu(String tenchucvu) {
		super();
		this.tenchucvu = tenchucvu;
	}


	
	
}

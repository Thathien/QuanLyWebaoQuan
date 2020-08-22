package com.banhang.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "CHUCVU")
public class ChucVu {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	int machucvu;
	String tenchucvu;
	public ChucVu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChucVu(int machucvu, String tenchucvu) {
		super();
		this.machucvu = machucvu;
		this.tenchucvu = tenchucvu;
	}
	public int getMachucvu() {
		return machucvu;
	}
	public void setMachucvu(int machucvu) {
		this.machucvu = machucvu;
	}
	public String getTenchucvu() {
		return tenchucvu;
	}
	public void setTenchucvu(String tenchucvu) {
		this.tenchucvu = tenchucvu;
	}
	
}

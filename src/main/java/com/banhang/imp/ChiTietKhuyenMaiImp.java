package com.banhang.imp;

import java.util.List;

import com.banhang.entity.ChiTietKhuyenMai;

public interface ChiTietKhuyenMaiImp {
	public List<ChiTietKhuyenMai> getAllChiTietKhuyenMai();
	public int addNewKhuyenMaiChoSP(ChiTietKhuyenMai ctkm);
	public boolean deletKhuyenMaiSP(ChiTietKhuyenMai ctkm);
}

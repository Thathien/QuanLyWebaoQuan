package com.banhang.imp;

import java.util.List;

import com.banhang.entity.KhuyenMai;

public interface KhuyenMaiImp {
	public List<KhuyenMai> getAllKhuyenMai();
	public int addKhuyenMai(KhuyenMai km);
	public boolean updateKhuyenMai(KhuyenMai km);
	public boolean deleteKhuyenMai(KhuyenMai km);
	public boolean checkNameKhuyenMai(String name);
}

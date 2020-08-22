package com.banhang.imp;

import java.util.List;

import com.banhang.entity.MauSanPham;

public interface MauSanPhamImp {
	public List<MauSanPham> getAllMauSanPham();
	public int addMauSanPham(MauSanPham ms);
	public boolean updateMauSanPham(MauSanPham ms);
	public boolean deleteMauSanPham(MauSanPham ms);
	public boolean checkMauSanPham(String s);
}

package com.banhang.imp;



import java.util.List;

import com.banhang.entity.SanPham;

public interface SanPhamImp {
	public List<SanPham> getAllSanPham();
	public List<SanPham> getAllSanPhamByIdDanhMuc(int id);
	public SanPham getListSanPhamById(int id);
	public int addSanPham(SanPham sp);
	public boolean updateSanPham(SanPham sp);
	public boolean deleteSanPham(SanPham sp);
	public boolean checkNameSanPhamBeforeAdd(String tenSp);
}

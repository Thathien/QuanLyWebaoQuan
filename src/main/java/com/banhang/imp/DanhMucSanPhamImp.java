package com.banhang.imp;

import java.util.List;

import com.banhang.entity.DanhMucSanPham;

public interface DanhMucSanPhamImp {
	public List<DanhMucSanPham> getAllDanhMucSanPham();
	public int addDanhMucSanPham(DanhMucSanPham dm);
	public boolean updateDanhMucSanPham(DanhMucSanPham dm);
	public boolean deleteDanhMucSanPham(DanhMucSanPham dm);
	public boolean checkNameDanhMucBeforeAdd(String name);
}

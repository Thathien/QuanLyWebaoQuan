package com.banhang.imp;


import java.util.List;

import com.banhang.entity.SizeSanPham;

public interface SizeSanPhamImp {
	public List<SizeSanPham> getAllSizeSanPham();
	public int addSizeSanPham(SizeSanPham ssp);
	public boolean updateSizeSanPham(SizeSanPham ssp);
	public boolean deleteSizeSanPham(SizeSanPham ssp);
}

package com.banhang.imp;

import java.util.List;

import com.banhang.entity.ChiTietSanPham;

public interface ChiTietSanPhamImp {
	public List<ChiTietSanPham> getInforCTSanPhambyID(int masanpham);
	public boolean updateCTSanPham(ChiTietSanPham chiTietSanPham);
	public boolean deleteCTSanPham(ChiTietSanPham chiTietSanPham);
	public int addCTSanPham(ChiTietSanPham chiTietSanPham);
	public boolean deleteCTSanPhamByIdSanPham(int id) ;
		
}

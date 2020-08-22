package com.banhang.imp;

import java.util.List;

import com.banhang.entity.ChiTietHoaDon;

public interface ChiTietHoaDonImp {
	public List<ChiTietHoaDon> getListCTHoaDonByID(int mahoadon);
	public boolean deleteChiTietHoaDon(ChiTietHoaDon chiTietHoaDon);
	public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon);
	public int addChiTietHoaDon(ChiTietHoaDon chiTietHoaDon);
	public ChiTietHoaDon getInforChiTietHoaDon(int mahoadon,int machitietsanpham);
	public boolean checkExitsSanPham(int mahoadon,int machitietsanpham);
}

package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.ChiTietHoaDonDao;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.imp.ChiTietHoaDonImp;

@Service
public class ChiTietHoaDonService implements ChiTietHoaDonImp{

	@Autowired
	@Qualifier("chiTietHoaDonDao")
	ChiTietHoaDonDao chiTietHoaDonDao;
	
	public List<ChiTietHoaDon> getListCTHoaDonByID(int mahoadon) {
		// TODO Auto-generated method stub
		return chiTietHoaDonDao.getListCTHoaDonByID(mahoadon);
	}

	public boolean deleteChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		// TODO Auto-generated method stub
		return chiTietHoaDonDao.deleteChiTietHoaDon(chiTietHoaDon);
	}

	public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		// TODO Auto-generated method stub
		return chiTietHoaDonDao.updateChiTietHoaDon(chiTietHoaDon);
	}

	public int addChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		// TODO Auto-generated method stub
		return chiTietHoaDonDao.addChiTietHoaDon(chiTietHoaDon);
	}

	public ChiTietHoaDon getInforChiTietHoaDon(int mahoadon, int machitietsanpham) {
		// TODO Auto-generated method stub
		return chiTietHoaDonDao.getInforChiTietHoaDon(mahoadon, machitietsanpham);
	}

	public boolean checkExitsSanPham(int mahoadon, int machitietsanpham) {
		// TODO Auto-generated method stub
		return chiTietHoaDonDao.checkExitsSanPham(mahoadon, machitietsanpham);
	}
	
}

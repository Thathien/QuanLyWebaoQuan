package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.HoaDonDao;
import com.banhang.entity.HoaDon;
import com.banhang.imp.HoaDonImp;

@Service
public class HoaDonService implements HoaDonImp{

	@Autowired
	@Qualifier("hoaDonDao")
	HoaDonDao hoaDonDao;
	
	public List<HoaDon> getListCanDuyetHoaDonAdmin() {
		// TODO Auto-generated method stub
		return hoaDonDao.getListCanDuyetHoaDonAdmin();
	}

	public List<HoaDon> getListHoaDonUser(String hoten) {
		// TODO Auto-generated method stub
		return hoaDonDao.getListHoaDonUser(hoten);
	}

	public List<HoaDon> getListHoaDonDaDuyet() {
		// TODO Auto-generated method stub
		return hoaDonDao.getListHoaDonDaDuyet();
	}

	public int addNewHoaDonUser(HoaDon hoaDon) {
		// TODO Auto-generated method stub
		return hoaDonDao.addNewHoaDonUser(hoaDon);
	}

	public boolean updateHoaDon(HoaDon hoaDon) {
		// TODO Auto-generated method stub
		return hoaDonDao.updateHoaDon(hoaDon);
	}

}

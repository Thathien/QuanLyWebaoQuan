package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.ChiTietKhuyenMaiDao;
import com.banhang.entity.ChiTietKhuyenMai;
import com.banhang.imp.ChiTietKhuyenMaiImp;

@Service
public class ChiTietKhuyenMaiService implements ChiTietKhuyenMaiImp{

	@Autowired
	@Qualifier("chiTietKhuyenMaiDao")
	ChiTietKhuyenMaiDao chiTietKhuyenMaiDao;
	
	public List<ChiTietKhuyenMai> getAllChiTietKhuyenMai() {
		// TODO Auto-generated method stub
		return chiTietKhuyenMaiDao.getAllChiTietKhuyenMai();
	}

	public int addNewKhuyenMaiChoSP(ChiTietKhuyenMai ctkm) {
		// TODO Auto-generated method stub
		return chiTietKhuyenMaiDao.addNewKhuyenMaiChoSP(ctkm);
	}

	public boolean deletKhuyenMaiSP(ChiTietKhuyenMai ctkm) {
		// TODO Auto-generated method stub
		return chiTietKhuyenMaiDao.deletKhuyenMaiSP(ctkm);
	}

}

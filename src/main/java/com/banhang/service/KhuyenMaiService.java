package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.KhuyenMaiDao;
import com.banhang.entity.KhuyenMai;
import com.banhang.imp.KhuyenMaiImp;

@Service
public class KhuyenMaiService implements KhuyenMaiImp{
	
	@Autowired
	@Qualifier("khuyenMaiDao")
	KhuyenMaiDao khuyenMaiDao;
	
	public List<KhuyenMai> getAllKhuyenMai() {
		// TODO Auto-generated method stub
		return khuyenMaiDao.getAllKhuyenMai() ;
	}

	public int addKhuyenMai(KhuyenMai km) {
		// TODO Auto-generated method stub
		return khuyenMaiDao.addKhuyenMai(km);
	}

	public boolean updateKhuyenMai(KhuyenMai km) {
		// TODO Auto-generated method stub
		return khuyenMaiDao.updateKhuyenMai(km);
	}

	public boolean deleteKhuyenMai(KhuyenMai km) {
		// TODO Auto-generated method stub
		return khuyenMaiDao.deleteKhuyenMai(km);
	}

	public boolean checkNameKhuyenMai(String name) {
		// TODO Auto-generated method stub
		return khuyenMaiDao.checkNameKhuyenMai(name);
	}
	
}

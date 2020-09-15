package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.SizeSanPhamDao;
import com.banhang.imp.SizeSanPhamImp;

@Service
public class SizeSanPhamService implements SizeSanPhamImp{

	@Autowired
	@Qualifier("sizeSanPhamDao")
	SizeSanPhamDao sizeSanPhamDao;

	public List<com.banhang.entity.SizeSanPham> getAllSizeSanPham() {
		// TODO Auto-generated method stub
		return sizeSanPhamDao.getAllSizeSanPham();
	}

	public int addSizeSanPham(com.banhang.entity.SizeSanPham ssp) {
		// TODO Auto-generated method stub
		return sizeSanPhamDao.addSizeSanPham(ssp);
	}

	public boolean updateSizeSanPham(com.banhang.entity.SizeSanPham ssp) {
		// TODO Auto-generated method stub
		return sizeSanPhamDao.updateSizeSanPham(ssp);
	}

	public boolean deleteSizeSanPham(com.banhang.entity.SizeSanPham ssp) {
		// TODO Auto-generated method stub
		return sizeSanPhamDao.deleteSizeSanPham(ssp);
	}
	

}

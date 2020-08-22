package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.MauSanPhamDao;
import com.banhang.entity.MauSanPham;
import com.banhang.imp.MauSanPhamImp;

@Service
public class MauSanPhamService implements MauSanPhamImp{
	
	@Autowired
	@Qualifier("mauSanPhamDao")
	MauSanPhamDao mauSanPhamDao;
	
	public List<MauSanPham> getAllMauSanPham() {
		// TODO Auto-generated method stub
		return mauSanPhamDao.getAllMauSanPham();
	}

	public int addMauSanPham(MauSanPham ms) {
		// TODO Auto-generated method stub
		return mauSanPhamDao.addMauSanPham(ms);
	}

	public boolean updateMauSanPham(MauSanPham ms) {
		// TODO Auto-generated method stub
		return mauSanPhamDao.updateMauSanPham(ms);
	}

	public boolean deleteMauSanPham(MauSanPham ms) {
		// TODO Auto-generated method stub
		return mauSanPhamDao.deleteMauSanPham(ms);
	}

	public boolean checkMauSanPham(String s) {
		// TODO Auto-generated method stub
		return mauSanPhamDao.checkMauSanPham(s);
	}
	
}

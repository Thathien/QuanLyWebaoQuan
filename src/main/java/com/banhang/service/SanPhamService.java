package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banhang.dao.SanPhamDao;
import com.banhang.entity.SanPham;
import com.banhang.imp.SanPhamImp;

@Service
public class SanPhamService implements SanPhamImp{

	@Autowired
	SanPhamDao sanPhamDao;
	
	public List<SanPham> getAllSanPham() {
		// TODO Auto-generated method stub
		return sanPhamDao.getAllSanPham();
	}

	public List<SanPham> getAllSanPhamByIdDanhMuc(int id) {
		// TODO Auto-generated method stub
		return sanPhamDao.getAllSanPhamByIdDanhMuc(id);
	}

	public SanPham getListSanPhamById(int id) {
		// TODO Auto-generated method stub
		return sanPhamDao.getListSanPhamById(id);
	}

	public int addSanPham(SanPham sp) {
		// TODO Auto-generated method stub
		return sanPhamDao.addSanPham(sp);
	}

	public boolean updateSanPham(SanPham sp) {
		// TODO Auto-generated method stub
		return sanPhamDao.updateSanPham(sp);
	}

	public boolean deleteSanPham(SanPham sp) {
		// TODO Auto-generated method stub
		return sanPhamDao.deleteSanPham(sp);
	}

	public boolean checkNameSanPhamBeforeAdd(String tenSp) {
		// TODO Auto-generated method stub
		return sanPhamDao.checkNameSanPhamBeforeAdd(tenSp);
	}
	
}

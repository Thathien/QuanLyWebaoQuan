package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banhang.dao.DanhMucSanPhamDao;
import com.banhang.entity.DanhMucSanPham;
import com.banhang.imp.DanhMucSanPhamImp;

@Service
public class DanhMucSanPhamService implements DanhMucSanPhamImp{
	
	@Autowired
	DanhMucSanPhamDao danhMucSanPhamDao;
	
	public List<DanhMucSanPham> getAllDanhMucSanPham() {
		// TODO Auto-generated method stub
		return danhMucSanPhamDao.getAllDanhMucSanPham();
	}
	
	public int addDanhMucSanPham(DanhMucSanPham dm) {
		// TODO Auto-generated method stub
		return danhMucSanPhamDao.addDanhMucSanPham(dm);
	}

	public boolean updateDanhMucSanPham(DanhMucSanPham dm) {
		// TODO Auto-generated method stub
		return danhMucSanPhamDao.updateDanhMucSanPham(dm);
	}

	public boolean deleteDanhMucSanPham(DanhMucSanPham dm) {
		// TODO Auto-generated method stub
		return danhMucSanPhamDao.deleteDanhMucSanPham(dm);
	}

	public boolean checkNameDanhMucBeforeAdd(String name) {
		// TODO Auto-generated method stub
		return danhMucSanPhamDao.checkNameDanhMucBeforeAdd(name);
	}

}

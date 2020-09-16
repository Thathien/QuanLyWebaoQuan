package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.ChiTietSanPhamDao;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.imp.ChiTietSanPhamImp;

@Service
public class ChiTietSanPhamService implements ChiTietSanPhamImp{

	@Autowired
	@Qualifier("chiTietSanPhamDao")
	ChiTietSanPhamDao chiTietSanPhamDao;
	
	public List<ChiTietSanPham> getInforCTSanPhambyID(int masanpham) {
		// TODO Auto-generated method stub
		return chiTietSanPhamDao.getInforCTSanPhambyID(masanpham);
	}

	public boolean updateCTSanPham(ChiTietSanPham chiTietSanPham) {
		// TODO Auto-generated method stub
		return chiTietSanPhamDao.updateCTSanPham(chiTietSanPham);
	}

	public boolean deleteCTSanPham(ChiTietSanPham chiTietSanPham) {
		// TODO Auto-generated method stub
		return chiTietSanPhamDao.deleteCTSanPham(chiTietSanPham);
	}

	public int addCTSanPham(ChiTietSanPham chiTietSanPham) {
		// TODO Auto-generated method stub
		return chiTietSanPhamDao.addCTSanPham(chiTietSanPham);
	}

	@Override
	public boolean deleteCTSanPhamByIdSanPham(int id) {
		// TODO Auto-generated method stub
		return chiTietSanPhamDao.deleteCTSanPhamByIdSanPham(id);
	}
	
	
}

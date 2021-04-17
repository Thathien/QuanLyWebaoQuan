package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.ChucVuDao;
import com.banhang.entity.ChucVu;
import com.banhang.imp.ChucVuImp;

@Service
public class ChucVuService implements ChucVuImp{

	@Autowired
	@Qualifier("chucVuDao")
	ChucVuDao chucVuDao;
	
	public List<ChucVu> getAllChucVu() {
		// TODO Auto-generated method stub
		return chucVuDao.getAllChucVu();
	}

	public int addChucVu(ChucVu cv) {
		// TODO Auto-generated method stub
		return chucVuDao.addChucVu(cv);
	}

	public boolean updateChucVu(ChucVu cv) {
		// TODO Auto-generated method stub
		return chucVuDao.updateChucVu(cv);
	}

	public boolean deleteChucVu(ChucVu cv) {
		// TODO Auto-generated method stub
		return chucVuDao.deleteChucVu(cv);
	}

	public boolean checkNameChucVu(String name) {
		// TODO Auto-generated method stub
		return chucVuDao.checkNameChucVu(name);
	}

	@Override
	public ChucVu getById(int id) {
		List<ChucVu> chucVus=null;
		chucVus=getAllChucVu();
		
		ChucVu cv=null;
		for (ChucVu c : chucVus) {
			if(c.getMachucvu()==id) {
				cv=c;
			}
		}
		// TODO Auto-generated method stub
		return cv;
	}

}

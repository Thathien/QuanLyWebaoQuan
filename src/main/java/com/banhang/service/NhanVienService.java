package com.banhang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.banhang.dao.NhanVienDao;
import com.banhang.entity.NhanVien;
import com.banhang.imp.NhanVienImp;

@Service
public class NhanVienService implements NhanVienImp{
	
	@Autowired
	@Qualifier("nhanVienDao")
	NhanVienDao nhanVienDao;

	public boolean login(String email, String matkhau) {
		// TODO Auto-generated method stub
		return nhanVienDao.login(email, matkhau);
	}

	public boolean checkUser(String email, String matkhau) {
		// TODO Auto-generated method stub
		return nhanVienDao.checkUser(email, matkhau);
	}

	public NhanVien getInforby_User_Pass(String email, String matkhau) {
		// TODO Auto-generated method stub
		return nhanVienDao.getInforby_User_Pass(email, matkhau);
	}

	public boolean checkEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkUserName(String TDN) {
		// TODO Auto-generated method stub
		return nhanVienDao.checkUserName(TDN);
	}

	public NhanVien getInforbyId(int id) {
		// TODO Auto-generated method stub
		return nhanVienDao.getInforbyId(id);
	}

	public int register(NhanVien nv) {
		// TODO Auto-generated method stub
		return nhanVienDao.register(nv);
	}

	public List<NhanVien> getallTaiKhoan() {
		// TODO Auto-generated method stub
		return nhanVienDao.getallTaiKhoan();
	}

	public boolean checkAdmin(String email, String matkhau) {
		// TODO Auto-generated method stub
		return nhanVienDao.checkAdmin(email, matkhau);
	}

	public boolean updateInfor(NhanVien nv) {
		// TODO Auto-generated method stub
		return nhanVienDao.updateInfor(nv);
	}

	public boolean deleteNhanVien(NhanVien nv) {
		// TODO Auto-generated method stub
		return nhanVienDao.deleteNhanVien(nv);
	}
	
}

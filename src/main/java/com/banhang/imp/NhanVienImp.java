package com.banhang.imp;


import java.util.List;

import com.banhang.entity.NhanVien;

public interface NhanVienImp {
	
	public boolean login(String email,String matkhau);
	
	public boolean checkUser(String email,String matkhau);
	
	public NhanVien getInforby_User_Pass(String email,String matkhau);
	
	public boolean checkEmail(String email);
	
	public boolean checkUserName(String TDN);
	
	public NhanVien getInforbyId(int id);
	
	public int register(NhanVien nv);
	
	public boolean updateInfor(NhanVien nv);
	
	public boolean deleteNhanVien(NhanVien nv);
	
	public List<NhanVien>  getallTaiKhoan();
	
	public boolean checkAdmin(String email,String matkhau);
	
}

package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.NhanVien;
import com.banhang.imp.NhanVienImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NhanVienDao implements NhanVienImp{
	
	@Autowired
	SessionFactory sessionFactory;

	//kiểm tra user pass
	@Transactional
	public boolean login(String email, String matkhau) {
		
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where tendangnhap='"+email+"' and matkhau='"+matkhau+"'";
		List<NhanVien> listNhanVien=session.createQuery(sql).getResultList();
		if(!listNhanVien.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	//Kiểm tra là người dùng(khách mã chức vụ = 3)
	@Transactional
	public boolean checkUser(String email, String matkhau) {
		
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where tendangnhap='"+email+"' and matkhau='"+matkhau+"' and machucvu=3";
		List<NhanVien> listNhanVien=session.createQuery(sql).getResultList();
		if(!listNhanVien.isEmpty()) {
			return true;
		}
		return false;
	}
	
	//lấy thông tin người dùng thông qua tên đăng nhập, mật khẩu
	@Transactional
	public NhanVien getInforby_User_Pass(String email, String matkhau) {
		
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where tendangnhap='"+email+"' and matkhau='"+matkhau+"'";
		NhanVien nv= (NhanVien) session.createQuery(sql).getSingleResult();
		if(nv!=null) {
			return nv;
		}
		return null;
	}
	
	//kiểm tra email trùng,nếu trùng -> true
	@Transactional
	public boolean checkEmail(String email) {
		
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where email='"+email+"'";
		List<NhanVien> listNV=session.createQuery(sql).getResultList();
		if(!listNV.isEmpty()) {
			return true;
		}
		return false;
	}

	//kiểm tra tên đăng nhập trùng nếu trùng ->true
	@Transactional
	public boolean checkUserName(String TDN) {
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where tendangnhap='"+TDN+"'";
		NhanVien nv= (NhanVien) session.createQuery(sql).getSingleResult();
		if(nv!=null) {
			return true;
		}
		return false;
	}

	//lấy thông tin tài khoản bằng ID (mã tài khoản)
	@Transactional
	public NhanVien getInforbyId(int id) {
		
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where manhanvien='"+id+"'";
		NhanVien nv= (NhanVien) session.createQuery(sql).getSingleResult();
		return nv;
	}

	//thêm mới người dùng
	@Transactional
	public int register(NhanVien nv) {
		
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(nv);
		return id;
	}

	//Lấy toàn bộ thông tin tài khoản
	@Transactional
	public List<NhanVien> getallTaiKhoan() {
		
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where machucvu=1 ";
		List<NhanVien> listNhanVien=session.createQuery(sql).getResultList();
		return listNhanVien;
	}

	//Kiểm tra là admin(khách mã chức vụ = 1)
	@Transactional
	public boolean checkAdmin(String email, String matkhau) {
		Session session=sessionFactory.getCurrentSession();
		String  sql="from NHANVIEN where tendangnhap='"+email+"' and matkhau='"+matkhau+"' and machucvu=1";
		NhanVien nv= (NhanVien) session.createQuery(sql).getSingleResult();
		if(nv!=null) {
			return true;
		}
		return false;
	}
	
	//Cập nhật thông tin người dùng
	@Transactional
	public boolean updateInfor(NhanVien nv) {
		Session session=sessionFactory.getCurrentSession();
		session.update(nv);
		return true;
	}

	//Xóa người dùng
	@Transactional
	public boolean deleteNhanVien(NhanVien nv) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(nv);
		return true;
	}
	
	
	

}

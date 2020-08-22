package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.DanhMucSanPham;
import com.banhang.imp.DanhMucSanPhamImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DanhMucSanPhamDao implements DanhMucSanPhamImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<DanhMucSanPham> getAllDanhMucSanPham() {
		Session session=sessionFactory.getCurrentSession();
		String sql="from DANHMUCSANPHAM";
		List<DanhMucSanPham> listDanhMuc=(List<DanhMucSanPham>)session.createQuery(sql).getResultList();
		return listDanhMuc;
	}
	
	public int addDanhMucSanPham(DanhMucSanPham dm) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(dm);
		return id;
	}

	public boolean updateDanhMucSanPham(DanhMucSanPham dm) {
		Session session=sessionFactory.getCurrentSession();
		session.update(dm);
		return true;
	}

	public boolean deleteDanhMucSanPham(DanhMucSanPham dm) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(dm);
		return true;
	}

	public boolean checkNameDanhMucBeforeAdd(String name) {
		Session session=sessionFactory.getCurrentSession();
		String sql = "from DANHMUCSANPHAM where tendanhmuc='"+name+"'";
		String kq=(String) session.createQuery(sql).getSingleResult();
		//Kiểm tra tên trùng sản phẩm nếu trùng -> trả về true || trả về false trên không trùng
		if(kq.equals(name)){return true;}
		return false;
	}
}
	

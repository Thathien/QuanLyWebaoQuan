package com.banhang.dao;

import com.banhang.entity.SanPham;
import com.banhang.imp.SanPhamImp;



import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SanPhamDao implements SanPhamImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<SanPham> getAllSanPham() {
		Session session=sessionFactory.getCurrentSession();
		String sql="from SANPHAM ";
		List<SanPham> listSanPham=(List<SanPham>) session.createQuery(sql).getResultList();
		return listSanPham;
	}
	
	@Transactional
	public List<SanPham> getAllSanPhamByIdDanhMuc(int id) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from SANPHAM where madanhmucsanpham= "+id+"";
		List<SanPham> listSanPhamByIdDM=(List<SanPham>) session.createQuery(sql).getResultList();
		return listSanPhamByIdDM;
	}
	
	@Transactional
	public SanPham getListSanPhamById(int id) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from SANPHAM  where masanpham=" +id +"";
		SanPham sanPhamById= (SanPham) session.createQuery(sql).getSingleResult();
		return sanPhamById;
	}

	@Transactional
	public int addSanPham(SanPham sp) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(sp);
		return id;
	}
	
	@Transactional
	public boolean updateSanPham(SanPham sp) {
		Session session=sessionFactory.getCurrentSession();
		session.update(sp);
		return true;
	}

	@Transactional
	public boolean deleteSanPham(SanPham sp) {
		Session session=sessionFactory.getCurrentSession();
		session.update(sp);
		return true;
	}
	
	@Transactional
	public boolean checkNameSanPhamBeforeAdd(String tenSp) {
		Session session=sessionFactory.getCurrentSession();
		String sql = "from SANPHAM where tensanpham='"+tenSp+"'";
		String kq=(String) session.createQuery(sql).getSingleResult();
		//Kiểm tra tên trùng sản phẩm nếu trùng -> trả về true || trả về false trên không trùng
		if(kq.equals(tenSp)){return true;}
		return false;
	}
	
}

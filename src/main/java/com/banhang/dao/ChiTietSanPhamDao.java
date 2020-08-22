package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.ChiTietSanPham;
import com.banhang.imp.ChiTietSanPhamImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChiTietSanPhamDao implements ChiTietSanPhamImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<ChiTietSanPham> getInforCTSanPhambyID(int masanpham) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHITIETSANPHAM where masanpham='"+masanpham+"'";
		List<ChiTietSanPham> ctSP=session.createQuery(sql).getResultList();
		return ctSP;
	}

	@Transactional
	public boolean updateCTSanPham(ChiTietSanPham chiTietSanPham) {
		Session session=sessionFactory.getCurrentSession();
		session.update(chiTietSanPham);
		return true;
	}

	@Transactional
	public boolean deleteCTSanPham(ChiTietSanPham chiTietSanPham) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(chiTietSanPham);
		return true;
	}

	@Transactional
	public int addCTSanPham(ChiTietSanPham chiTietSanPham) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(chiTietSanPham);
		return id;
	}

}

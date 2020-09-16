package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.SizeSanPham;
import com.banhang.imp.SizeSanPhamImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SizeSanPhamDao implements SizeSanPhamImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<SizeSanPham> getAllSizeSanPham() {
		Session session=sessionFactory.getCurrentSession();
		String sql="from SIZESANPHAM";
		List<SizeSanPham> listSizeSanPham=session.createQuery(sql).getResultList();
		return listSizeSanPham;
	}

	@Override
	@Transactional
	public int addSizeSanPham(SizeSanPham ssp) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(ssp);
		return id;
	}

	@Override
	@Transactional
	public boolean updateSizeSanPham(SizeSanPham ssp) {
		Session session=sessionFactory.getCurrentSession();
		session.update(ssp);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteSizeSanPham(SizeSanPham ssp) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(ssp);
		return true;
	}
	
}

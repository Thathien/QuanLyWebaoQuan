package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.banhang.entity.MauSanPham;
import com.banhang.imp.MauSanPhamImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MauSanPhamDao implements MauSanPhamImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<MauSanPham> getAllMauSanPham() {
		Session session=sessionFactory.getCurrentSession();
		String sql="from MAUSANPHAM";
		List<MauSanPham> listMauSanPham=session.createQuery(sql).getResultList();
		return listMauSanPham;
	}

	@Transactional
	public int addMauSanPham(MauSanPham ms) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(ms);
		return id;
	}

	@Transactional
	public boolean updateMauSanPham(MauSanPham ms) {
		Session session=sessionFactory.getCurrentSession();
		session.update(ms);
		return true;
	}

	@Transactional
	public boolean deleteMauSanPham(MauSanPham ms) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(ms);
		return true;
	}

	@Transactional
	public boolean checkMauSanPham(String s) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from MAUSANPHAM where tenmau='"+s+"'";
		MauSanPham ms= (MauSanPham) session.createQuery(sql).getSingleResult();
		if(ms!=null) {
			return true;
		}
		return false;
	}
	
}

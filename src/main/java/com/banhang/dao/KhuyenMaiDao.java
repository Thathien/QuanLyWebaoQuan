package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.KhuyenMai;
import com.banhang.imp.KhuyenMaiImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KhuyenMaiDao implements KhuyenMaiImp{
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<KhuyenMai> getAllKhuyenMai() {
		Session session=sessionFactory.getCurrentSession();
		String sql=" from KHUYENMAI";
		List<KhuyenMai> listKhuyenMai=session.createQuery(sql).getResultList();
		return listKhuyenMai;
	}
	
	@Transactional
	public int addKhuyenMai(KhuyenMai km) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(km);
		return id;
	}
	
	@Transactional
	public boolean updateKhuyenMai(KhuyenMai km) {
		Session session=sessionFactory.getCurrentSession();
		session.update(km);
		return true;
	}
	
	@Transactional
	public boolean deleteKhuyenMai(KhuyenMai km) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(km);
		return true;
	}
	
	@Transactional
	public boolean checkNameKhuyenMai(String name) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from KHUYENMAI where tenkhuyenmai='"+name+"'";
		KhuyenMai km=(KhuyenMai) session.createQuery(sql).getSingleResult();
		if(km!=null) {
			return true;
		}
		return false;
	}
	
}

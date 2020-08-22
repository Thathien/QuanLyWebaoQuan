package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.ChiTietKhuyenMai;
import com.banhang.imp.ChiTietKhuyenMaiImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChiTietKhuyenMaiDao implements ChiTietKhuyenMaiImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<ChiTietKhuyenMai> getAllChiTietKhuyenMai() {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHITIETKHUYENMAI";
		List<ChiTietKhuyenMai> listChiTietKM=session.createQuery(sql).getResultList();
		return listChiTietKM;
	}

	@Transactional
	public int addNewKhuyenMaiChoSP(ChiTietKhuyenMai ctkm) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(ctkm);
		return id;
	}

	@Transactional
	public boolean deletKhuyenMaiSP(ChiTietKhuyenMai ctkm) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(ctkm);
		return true;
	}
	
}

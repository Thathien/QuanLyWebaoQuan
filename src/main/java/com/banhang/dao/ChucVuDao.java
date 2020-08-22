package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.ChucVu;
import com.banhang.imp.ChucVuImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChucVuDao implements ChucVuImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<ChucVu> getAllChucVu() {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHUCVU";
		List<ChucVu> listChucChu=session.createQuery(sql).getResultList();
		return listChucChu;
	}
	@Transactional
	public int addChucVu(ChucVu cv) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(cv);
		return id;
	}
	@Transactional
	public boolean updateChucVu(ChucVu cv) {
		Session session=sessionFactory.getCurrentSession();
		session.update(cv);
		return true;
	}
	@Transactional
	public boolean deleteChucVu(ChucVu cv) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(cv);
		return true;
	}
	@Transactional
	public boolean checkNameChucVu(String name) {
		Session session=sessionFactory.getCurrentSession();
		String sql=" from CHUCVU where tenchucvu='"+name+"'" ;
		ChucVu cv= (ChucVu) session.createQuery(sql).getSingleResult();
		if(cv!=null) {
			return true;
		}
		return false;
	}

}

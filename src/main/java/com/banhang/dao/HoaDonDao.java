package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.HoaDon;
import com.banhang.imp.HoaDonImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HoaDonDao implements HoaDonImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public List<HoaDon> getListCanDuyetHoaDonAdmin() {
		Session session=sessionFactory.getCurrentSession();
		//0-> đang xử lý ;1 -> đã duyệt;2-> đang giao;3-> đã hoàn thành
		String Sql="from HOADON where tinhtrang=0";
		List<HoaDon> listHoaDon=session.createQuery(Sql).getResultList();
		return listHoaDon;
	}

	@Transactional
	public List<HoaDon> getListHoaDonUser(String hoten) {
		Session session=sessionFactory.getCurrentSession();
		String Sql="from HOADON where tenkhachhang='"+hoten+"'";
		List<HoaDon> listHoaDon=session.createQuery(Sql).getResultList();
		return listHoaDon;
	}

	@Transactional
	public List<HoaDon> getListHoaDonDaDuyet() {
		Session session=sessionFactory.getCurrentSession();
		//0-> đang xử lý ;1 -> đã duyệt;2-> đang giao;3-> đã hoàn thành
		String Sql="from HOADON where tinhtrang=0";
		List<HoaDon> listHoaDon=session.createQuery(Sql).getResultList();
		return listHoaDon;
	}

	@Transactional
	public int addNewHoaDonUser(HoaDon hoaDon) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(hoaDon);
		return id;
	}

	@Transactional
	public boolean updateHoaDon(HoaDon hoaDon) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(hoaDon);
		return true;
	}


}

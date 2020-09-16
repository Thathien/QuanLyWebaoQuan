package com.banhang.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.ChiTietHoaDon;
import com.banhang.imp.ChiTietHoaDonImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChiTietHoaDonDao implements ChiTietHoaDonImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ChiTietHoaDon> getListCTHoaDonByID(int mahoadon) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHITIETHOADON where mahoadon='"+mahoadon+"'";
		List<ChiTietHoaDon> listChiTietHoaDon=session.createQuery(sql).getResultList();
		return listChiTietHoaDon;
	}

	@Override
	@Transactional
	public boolean deleteChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(chiTietHoaDon);
		return false;
	}
	
	@Override
	@Transactional
	public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(chiTietHoaDon);
		return false;
	}

	@Override
	@Transactional
	public int addChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(chiTietHoaDon);
		return id;
	}

	@Override
	@Transactional
	public ChiTietHoaDon getInforChiTietHoaDon(int mahoadon, int machitietsanpham) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHITIETHOADON where mahoadon='"+mahoadon+"' and machitietsanpham='"+machitietsanpham+"'";
		ChiTietHoaDon ctHD=(ChiTietHoaDon) session.createQuery(sql).getSingleResult();
		return ctHD;
	}

	@Override
	@Transactional
	public boolean checkExitsSanPham(int mahoadon, int machitietsanpham) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHITIETHOADON where mahoadon='"+mahoadon+"' and machitietsanpham='"+machitietsanpham+"'";
		ChiTietHoaDon ctHD=(ChiTietHoaDon) session.createQuery(sql).getSingleResult();
		if(ctHD!=null) {
			return true;
		}
		return false;
	}

}

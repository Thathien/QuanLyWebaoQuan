package com.banhang.dao;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoaDonId;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.SanPham;
import com.banhang.imp.ChiTietSanPhamImp;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChiTietSanPhamDao implements ChiTietSanPhamImp{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ChiTietSanPham> getInforCTSanPhambyID(int masanpham) {
		Session session=sessionFactory.getCurrentSession();
		String sql="from CHITIETSANPHAM where masanpham='"+masanpham+"'";
		List<ChiTietSanPham> ctSP=session.createQuery(sql).getResultList();
		return ctSP;
	}

	@Override
	@Transactional
	public boolean updateCTSanPham(ChiTietSanPham chiTietSanPham) {
		Session session=sessionFactory.getCurrentSession();
		session.update(chiTietSanPham);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteCTSanPham(ChiTietSanPham chiTietSanPham) {
		Session session=sessionFactory.getCurrentSession();
		session.delete(chiTietSanPham);
		return true;
	}

	@Override
	@Transactional
	public int addCTSanPham(ChiTietSanPham chiTietSanPham) {
		Session session=sessionFactory.getCurrentSession();
		int id=(Integer) session.save(chiTietSanPham);
		return id;
	}

	@Override
	@Transactional
	public boolean deleteCTSanPhamByIdSanPham(int id) {
		Session session=sessionFactory.getCurrentSession();
		SanPham sanPham=session.get(SanPham.class,id);
		
		Set<ChiTietSanPham >chiTietSanPhamList=(Set) sanPham.getChiTietSanPham();
		
		for (ChiTietSanPham chiTietSanPham : chiTietSanPhamList ) {
			session.createQuery("delete CHITIETHOADON WHERE machitietsanpham="+chiTietSanPham.getMachitietsanpham());
		}
		session.createQuery("delete CHITIETKHUYENMAI WHERE masanpham="+id).executeUpdate();
		session.createQuery("delete CHITIETSANPHAM WHERE masanpham="+id).executeUpdate();
		session.createQuery("delete SANPHAM WHERE masanpham="+id).executeUpdate();
		return true;
	}

}

package com.banhang.dao;

import com.banhang.entity.ChiTietSanPham;
import com.banhang.imp.ChiTietSanPhamImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
@Transactional
public class ChiTietSanPhamDao implements ChiTietSanPhamImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<ChiTietSanPham> getInforCTSanPhambyID(int masanpham) {
    return entityManager
        .createQuery(
            "FROM ChiTietSanPham c WHERE c.sanPham.masanpham = :masanpham", ChiTietSanPham.class)
        .setParameter("masanpham", masanpham)
        .getResultList();
  }

  @Override
  public boolean updateCTSanPham(ChiTietSanPham chiTietSanPham) {
    entityManager.merge(chiTietSanPham);
    return true;
  }

  @Override
  public boolean deleteCTSanPham(ChiTietSanPham chiTietSanPham) {
    entityManager.remove(
        entityManager.contains(chiTietSanPham)
            ? chiTietSanPham
            : entityManager.merge(chiTietSanPham));
    return true;
  }

  @Override
  public int addCTSanPham(ChiTietSanPham chiTietSanPham) {
    entityManager.persist(chiTietSanPham);
    return chiTietSanPham.getMachitietsanpham();
  }

  @Override
  public boolean deleteCTSanPhamByIdSanPham(int id) {

    entityManager
        .createQuery("DELETE FROM ChiTietSanPham c WHERE c.sanPham.masanpham = :masanpham")
        .setParameter("masanpham", id)
        .executeUpdate();

    return true;
  }
}

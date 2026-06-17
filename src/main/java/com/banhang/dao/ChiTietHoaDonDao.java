package com.banhang.dao;

import com.banhang.entity.ChiTietHoaDon;
import com.banhang.imp.ChiTietHoaDonImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ChiTietHoaDonDao implements ChiTietHoaDonImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<ChiTietHoaDon> getListCTHoaDonByID(int mahoadon) {

    TypedQuery<ChiTietHoaDon> query =
        entityManager.createQuery(
            "SELECT c FROM ChiTietHoaDon c WHERE c.mahoadon = :mahoadon", ChiTietHoaDon.class);

    query.setParameter("mahoadon", mahoadon);

    return query.getResultList();
  }

  @Override
  public boolean deleteChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
    try {
      entityManager.remove(
          entityManager.contains(chiTietHoaDon)
              ? chiTietHoaDon
              : entityManager.merge(chiTietHoaDon));

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean updateChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
    try {
      entityManager.merge(chiTietHoaDon);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public int addChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
    try {
      entityManager.persist(chiTietHoaDon);
      return 1;
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ChiTietHoaDon getInforChiTietHoaDon(int mahoadon, int machitietsanpham) {

    TypedQuery<ChiTietHoaDon> query =
        entityManager.createQuery(
            """
                        SELECT c
                        FROM ChiTietHoaDon c
                        WHERE c.mahoadon = :mahoadon
                          AND c.machitietsanpham = :machitietsanpham
                        """,
            ChiTietHoaDon.class);

    query.setParameter("mahoadon", mahoadon);
    query.setParameter("machitietsanpham", machitietsanpham);

    return query.getResultStream().findFirst().orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkExitsSanPham(int mahoadon, int machitietsanpham) {

    return getInforChiTietHoaDon(mahoadon, machitietsanpham) != null;
  }
}

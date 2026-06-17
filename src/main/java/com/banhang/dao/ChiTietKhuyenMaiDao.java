package com.banhang.dao;

import com.banhang.entity.ChiTietKhuyenMai;
import com.banhang.imp.ChiTietKhuyenMaiImp;
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
public class ChiTietKhuyenMaiDao implements ChiTietKhuyenMaiImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<ChiTietKhuyenMai> getAllChiTietKhuyenMai() {

    TypedQuery<ChiTietKhuyenMai> query =
        entityManager.createQuery("SELECT c FROM ChiTietKhuyenMai c", ChiTietKhuyenMai.class);

    return query.getResultList();
  }

  @Override
  public int addNewKhuyenMaiChoSP(ChiTietKhuyenMai ctkm) {
    try {
      entityManager.persist(ctkm);
      return 1;
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public boolean deletKhuyenMaiSP(ChiTietKhuyenMai ctkm) {
    try {
      entityManager.remove(entityManager.contains(ctkm) ? ctkm : entityManager.merge(ctkm));

      return true;
    } catch (Exception e) {
      return false;
    }
  }
}

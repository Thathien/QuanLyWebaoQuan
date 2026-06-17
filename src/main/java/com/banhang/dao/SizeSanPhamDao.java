package com.banhang.dao;

import com.banhang.entity.SizeSanPham;
import com.banhang.imp.SizeSanPhamImp;
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
public class SizeSanPhamDao implements SizeSanPhamImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<SizeSanPham> getAllSizeSanPham() {

    TypedQuery<SizeSanPham> query =
        entityManager.createQuery("SELECT s FROM SizeSanPham s", SizeSanPham.class);

    return query.getResultList();
  }

  @Override
  public int addSizeSanPham(SizeSanPham ssp) {

    entityManager.persist(ssp);

    return ssp.getMasize();
  }

  @Override
  public boolean updateSizeSanPham(SizeSanPham ssp) {

    entityManager.merge(ssp);

    return true;
  }

  @Override
  public boolean deleteSizeSanPham(SizeSanPham ssp) {

    entityManager.remove(entityManager.contains(ssp) ? ssp : entityManager.merge(ssp));

    return true;
  }
}

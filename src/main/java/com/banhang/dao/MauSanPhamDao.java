package com.banhang.dao;

import com.banhang.entity.MauSanPham;
import com.banhang.imp.MauSanPhamImp;
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
public class MauSanPhamDao implements MauSanPhamImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<MauSanPham> getAllMauSanPham() {

    TypedQuery<MauSanPham> query =
        entityManager.createQuery("SELECT m FROM MauSanPham m", MauSanPham.class);

    return query.getResultList();
  }

  @Override
  public int addMauSanPham(MauSanPham ms) {

    entityManager.persist(ms);

    return ms.getMamau();
  }

  @Override
  public boolean updateMauSanPham(MauSanPham ms) {

    entityManager.merge(ms);

    return true;
  }

  @Override
  public boolean deleteMauSanPham(MauSanPham ms) {

    entityManager.remove(entityManager.contains(ms) ? ms : entityManager.merge(ms));

    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkMauSanPham(String tenMau) {

    TypedQuery<MauSanPham> query =
        entityManager.createQuery(
            """
                        SELECT m
                        FROM MauSanPham m
                        WHERE m.tenmau = :tenmau
                        """,
            MauSanPham.class);

    query.setParameter("tenmau", tenMau);

    return !query.setMaxResults(1).getResultList().isEmpty();
  }
}

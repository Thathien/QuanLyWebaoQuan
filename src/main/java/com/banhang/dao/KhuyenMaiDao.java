package com.banhang.dao;

import com.banhang.entity.KhuyenMai;
import com.banhang.imp.KhuyenMaiImp;
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
public class KhuyenMaiDao implements KhuyenMaiImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<KhuyenMai> getAllKhuyenMai() {

    TypedQuery<KhuyenMai> query =
        entityManager.createQuery("SELECT k FROM KhuyenMai k", KhuyenMai.class);

    return query.getResultList();
  }

  @Override
  public int addKhuyenMai(KhuyenMai km) {

    entityManager.persist(km);

    return km.getMakhuyenmai();
  }

  @Override
  public boolean updateKhuyenMai(KhuyenMai km) {

    entityManager.merge(km);

    return true;
  }

  @Override
  public boolean deleteKhuyenMai(KhuyenMai km) {

    entityManager.remove(entityManager.contains(km) ? km : entityManager.merge(km));

    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkNameKhuyenMai(String name) {

    TypedQuery<KhuyenMai> query =
        entityManager.createQuery(
            """
                        SELECT k
                        FROM KhuyenMai k
                        WHERE k.tenkhuyenmai = :name
                        """,
            KhuyenMai.class);

    query.setParameter("name", name);

    return !query.setMaxResults(1).getResultList().isEmpty();
  }
}

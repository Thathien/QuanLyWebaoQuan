package com.banhang.dao;

import com.banhang.entity.ChucVu;
import com.banhang.imp.ChucVuImp;
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
public class ChucVuDao implements ChucVuImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<ChucVu> getAllChucVu() {

    TypedQuery<ChucVu> query = entityManager.createQuery("SELECT c FROM ChucVu c", ChucVu.class);

    return query.getResultList();
  }

  @Override
  public int addChucVu(ChucVu cv) {

    entityManager.persist(cv);

    return cv.getMachucvu();
  }

  @Override
  public boolean updateChucVu(ChucVu cv) {

    entityManager.merge(cv);

    return true;
  }

  @Override
  public boolean deleteChucVu(ChucVu cv) {

    entityManager.remove(entityManager.contains(cv) ? cv : entityManager.merge(cv));

    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkNameChucVu(String name) {

    TypedQuery<ChucVu> query =
        entityManager.createQuery("SELECT c FROM ChucVu c WHERE c.tenchucvu = :name", ChucVu.class);

    query.setParameter("name", name);

    return !query.setMaxResults(1).getResultList().isEmpty();
  }

  @Override
  @Transactional(readOnly = true)
  public ChucVu getById(int id) {

    TypedQuery<ChucVu> query =
        entityManager.createQuery("SELECT c FROM ChucVu c WHERE c.machucvu = :id", ChucVu.class);

    query.setParameter("id", id);

    return query.getResultStream().findFirst().orElse(null);
  }
}

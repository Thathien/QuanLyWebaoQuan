package com.banhang.dao;

import com.banhang.entity.DanhMucSanPham;
import com.banhang.imp.DanhMucSanPhamImp;
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
public class DanhMucSanPhamDao implements DanhMucSanPhamImp {

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<DanhMucSanPham> getAllDanhMucSanPham() {

    TypedQuery<DanhMucSanPham> query =
        entityManager.createQuery("SELECT d FROM DanhMucSanPham d", DanhMucSanPham.class);

    return query.getResultList();
  }

  @Override
  public int addDanhMucSanPham(DanhMucSanPham dm) {

    entityManager.persist(dm);

    return dm.getMadanhmucsanpham();
  }

  @Override
  public boolean updateDanhMucSanPham(DanhMucSanPham dm) {

    entityManager.merge(dm);

    return true;
  }

  @Override
  public boolean deleteDanhMucSanPham(DanhMucSanPham dm) {

    entityManager.remove(entityManager.contains(dm) ? dm : entityManager.merge(dm));

    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkNameDanhMucBeforeAdd(String name) {

    TypedQuery<DanhMucSanPham> query =
        entityManager.createQuery(
            """
                        SELECT d
                        FROM DanhMucSanPham d
                        WHERE d.tendanhmuc = :name
                        """,
            DanhMucSanPham.class);

    query.setParameter("name", name);

    return !query.setMaxResults(1).getResultList().isEmpty();
  }
}

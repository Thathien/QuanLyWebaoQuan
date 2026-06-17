package com.banhang.dao;

import com.banhang.entity.SanPham;
import com.banhang.imp.SanPhamImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class SanPhamDao implements SanPhamImp {

  private static final Logger LOGGER = Logger.getLogger(SanPhamDao.class.getName());

  @PersistenceContext private EntityManager entityManager;

  @Override
  @Transactional(readOnly = true)
  public List<SanPham> getAllSanPham() {

    try {

      TypedQuery<SanPham> query =
          entityManager.createQuery("SELECT s FROM SanPham s", SanPham.class);

      return query.getResultList();

    } catch (Exception e) {

      LOGGER.severe("Error retrieving all products: " + e.getMessage());

      return List.of();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<SanPham> getAllSanPhamByIdDanhMuc(int id) {

    try {

      TypedQuery<SanPham> query =
          entityManager.createQuery(
              """
                            SELECT s
                            FROM SanPham s
                            WHERE s.madanhmucsanpham = :categoryId
                            """,
              SanPham.class);

      query.setParameter("categoryId", id);

      return query.getResultList();

    } catch (Exception e) {

      LOGGER.severe("Error retrieving products by category: " + e.getMessage());

      return List.of();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public SanPham getListSanPhamById(int id) {

    try {

      TypedQuery<SanPham> query =
          entityManager.createQuery(
              """
                            SELECT s
                            FROM SanPham s
                            WHERE s.masanpham = :productId
                            """,
              SanPham.class);

      query.setParameter("productId", id);

      List<SanPham> result = query.setMaxResults(1).getResultList();

      return result.isEmpty() ? null : result.get(0);

    } catch (Exception e) {

      LOGGER.severe("Error retrieving product by ID: " + e.getMessage());

      return null;
    }
  }

  @Override
  public int addSanPham(SanPham sp) {

    try {

      if (sp == null) {
        LOGGER.warning("Cannot add null product");
        return -1;
      }

      entityManager.persist(sp);

      return sp.getMasanpham();

    } catch (Exception e) {

      LOGGER.severe("Error adding product: " + e.getMessage());

      return -1;
    }
  }

  @Override
  public boolean updateSanPham(SanPham sp) {

    try {

      if (sp == null || sp.getMasanpham() == null) {
        LOGGER.warning("Cannot update null product or product without ID");
        return false;
      }

      entityManager.merge(sp);

      return true;

    } catch (Exception e) {

      LOGGER.severe("Error updating product: " + e.getMessage());

      return false;
    }
  }

  @Override
  public boolean deleteSanPham(SanPham sp) {

    try {

      if (sp == null || sp.getMasanpham() == null) {
        LOGGER.warning("Cannot delete null product or product without ID");
        return false;
      }

      entityManager.remove(entityManager.contains(sp) ? sp : entityManager.merge(sp));

      return true;

    } catch (Exception e) {

      LOGGER.severe("Error deleting product: " + e.getMessage());

      return false;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkNameSanPhamBeforeAdd(String tenSp) {

    try {

      if (tenSp == null || tenSp.trim().isEmpty()) {
        LOGGER.warning("Product name cannot be null or empty");
        return false;
      }

      TypedQuery<SanPham> query =
          entityManager.createQuery(
              """
                            SELECT s
                            FROM SanPham s
                            WHERE s.tensanpham = :productName
                            """,
              SanPham.class);

      query.setParameter("productName", tenSp.trim());

      return !query.setMaxResults(1).getResultList().isEmpty();

    } catch (Exception e) {

      LOGGER.severe("Error checking product name: " + e.getMessage());

      return false;
    }
  }
}

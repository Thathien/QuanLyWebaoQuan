package com.banhang.dao;

import com.banhang.entity.SanPham;
import com.banhang.imp.SanPhamImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    TypedQuery<SanPham> query = entityManager.createQuery("SELECT s FROM SanPham s", SanPham.class);
    return query.getResultList();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SanPham> getStorefrontProducts(
      Integer categoryId,
      String name,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      Pageable pageable) {
    StringBuilder where = new StringBuilder(" FROM san_pham p WHERE p.hiden = 0");
    if (categoryId != null) {
      where.append(" AND p.madanhmucsanpham = :categoryId");
    }
    if (name != null && !name.isBlank()) {
      where.append(" AND CHARINDEX(:name, p.tensanpham) > 0");
    }
    if (minPrice != null) {
      where.append(" AND TRY_CONVERT(decimal(19,2), p.giatien) >= :minPrice");
    }
    if (maxPrice != null) {
      where.append(" AND TRY_CONVERT(decimal(19,2), p.giatien) <= :maxPrice");
    }

    jakarta.persistence.Query countQuery =
        entityManager.createNativeQuery("SELECT COUNT(*)" + where);
    setStorefrontFilterParameters(countQuery, categoryId, name, minPrice, maxPrice);
    long total = ((Number) countQuery.getSingleResult()).longValue();
    if (pageable.getOffset() >= total) {
      return new PageImpl<>(List.of(), pageable, total);
    }

    jakarta.persistence.Query idQuery =
        entityManager.createNativeQuery("SELECT p.masanpham" + where + " ORDER BY p.masanpham");
    setStorefrontFilterParameters(idQuery, categoryId, name, minPrice, maxPrice);
    idQuery.setFirstResult(Math.toIntExact(pageable.getOffset()));
    idQuery.setMaxResults(pageable.getPageSize());

    @SuppressWarnings("unchecked")
    List<Number> ids = idQuery.getResultList();
    if (ids.isEmpty()) {
      return new PageImpl<>(List.of(), pageable, total);
    }

    List<Integer> productIds = new ArrayList<>(ids.size());
    for (Number id : ids) {
      productIds.add(id.intValue());
    }
    List<SanPham> products =
        entityManager
            .createQuery(
                "SELECT DISTINCT p FROM SanPham p WHERE p.masanpham IN :productIds "
                    + "ORDER BY p.masanpham",
                SanPham.class)
            .setParameter("productIds", productIds)
            .getResultList();
    return new PageImpl<>(products, pageable, total);
  }

  private void setStorefrontFilterParameters(
      jakarta.persistence.Query query,
      Integer categoryId,
      String name,
      BigDecimal minPrice,
      BigDecimal maxPrice) {
    if (categoryId != null) {
      query.setParameter("categoryId", categoryId);
    }
    if (name != null && !name.isBlank()) {
      query.setParameter("name", name.trim());
    }
    if (minPrice != null) {
      query.setParameter("minPrice", minPrice);
    }
    if (maxPrice != null) {
      query.setParameter("maxPrice", maxPrice);
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
                            WHERE s.danhMucSanPham.madanhmucsanpham = :categoryId
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
    if (sp == null || sp.getMasanpham() == null) {
      LOGGER.warning("Cannot delete null product or product without ID");
      return false;
    }

    entityManager.remove(entityManager.contains(sp) ? sp : entityManager.merge(sp));
    return true;
  }

  @Transactional(readOnly = true)
  public boolean hasProductHistory(int productId) {
    Number references =
        (Number)
            entityManager
                .createNativeQuery(
                    """
                    SELECT CASE WHEN EXISTS (
                      SELECT 1
                      FROM chi_tiet_hoa_don d
                      JOIN chi_tiet_san_pham v ON v.machitietsanpham = d.machitietsanpham
                      WHERE v.masanpham = :productId
                      UNION ALL
                      SELECT 1
                      FROM bien_dong_kho m
                      JOIN chi_tiet_san_pham v ON v.machitietsanpham = m.variant_id
                      WHERE v.masanpham = :productId
                      UNION ALL
                      SELECT 1
                      FROM chi_tiet_phieu_nhap d
                      JOIN chi_tiet_san_pham v ON v.machitietsanpham = d.variant_id
                      WHERE v.masanpham = :productId
                      UNION ALL
                      SELECT 1
                      FROM chi_tiet_hoan_tien d
                      JOIN chi_tiet_san_pham v ON v.machitietsanpham = d.variant_id
                      WHERE v.masanpham = :productId
                      UNION ALL
                      SELECT 1
                      FROM chi_tiet_tra_hang d
                      JOIN chi_tiet_san_pham v ON v.machitietsanpham = d.variant_id
                      WHERE v.masanpham = :productId
                    ) THEN 1 ELSE 0 END
                    """)
                .setParameter("productId", productId)
                .getSingleResult();
    return references.intValue() != 0;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkNameSanPhamBeforeAdd(String tenSp) {
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
  }
}

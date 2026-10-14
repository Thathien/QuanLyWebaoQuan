package com.banhang.dao;

import com.banhang.entity.TrangThaiYeuCauDoiTra;
import com.banhang.entity.YeuCauDoiTra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class YeuCauDoiTraDao {
  @PersistenceContext private EntityManager entityManager;

  public long requestedQuantity(int orderId, int variantId) {
    return entityManager
        .createQuery(
            """
            SELECT COALESCE(SUM(line.soLuong), 0)
            FROM ChiTietYeuCauDoiTra line
            WHERE line.yeuCau.hoaDon.mahoadon = :orderId
              AND line.bienThe.machitietsanpham = :variantId
              AND line.yeuCau.trangThai IN :statuses
            """,
            Long.class)
        .setParameter("orderId", orderId)
        .setParameter("variantId", variantId)
        .setParameter(
            "statuses", List.of(TrangThaiYeuCauDoiTra.CHO_XU_LY, TrangThaiYeuCauDoiTra.CHAP_NHAN))
        .getSingleResult();
  }

  @Transactional(readOnly = true)
  public List<YeuCauDoiTra> findByOrder(int orderId) {
    return entityManager
        .createQuery(
            """
            SELECT DISTINCT request FROM YeuCauDoiTra request
            JOIN FETCH request.hoaDon
            LEFT JOIN FETCH request.chiTiet line
            LEFT JOIN FETCH line.bienThe original
            LEFT JOIN FETCH original.sanPham
            LEFT JOIN FETCH original.sizeSanPham
            LEFT JOIN FETCH original.mauSanPham
            LEFT JOIN FETCH line.bienTheThayThe replacement
            LEFT JOIN FETCH replacement.sizeSanPham
            LEFT JOIN FETCH replacement.mauSanPham
            WHERE request.hoaDon.mahoadon = :orderId
            ORDER BY request.createdAt DESC
            """,
            YeuCauDoiTra.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  @Transactional(readOnly = true)
  public List<YeuCauDoiTra> findAll() {
    return entityManager
        .createQuery(
            """
            SELECT DISTINCT request FROM YeuCauDoiTra request
            JOIN FETCH request.hoaDon order
            LEFT JOIN FETCH request.chiTiet line
            LEFT JOIN FETCH line.bienThe original
            LEFT JOIN FETCH original.sanPham
            LEFT JOIN FETCH original.sizeSanPham
            LEFT JOIN FETCH original.mauSanPham
            LEFT JOIN FETCH line.bienTheThayThe replacement
            LEFT JOIN FETCH replacement.sizeSanPham
            LEFT JOIN FETCH replacement.mauSanPham
            ORDER BY request.createdAt DESC
            """,
            YeuCauDoiTra.class)
        .getResultList();
  }

  public YeuCauDoiTra findForUpdate(long id) {
    return entityManager.find(YeuCauDoiTra.class, id, LockModeType.PESSIMISTIC_WRITE);
  }

  public void persist(Object entity) {
    entityManager.persist(entity);
  }

  public void flush() {
    entityManager.flush();
  }
}

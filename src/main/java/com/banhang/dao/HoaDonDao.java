package com.banhang.dao;

import com.banhang.entity.HoaDon;
import com.banhang.imp.HoaDonImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO class cho entity HoaDon. Cung cấp các phương thức để thao tác dữ liệu hóa đơn từ database.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class HoaDonDao implements HoaDonImp {

  private static final Logger LOGGER = Logger.getLogger(HoaDonDao.class.getName());

  @PersistenceContext private EntityManager entityManager;

  /**
   * Lấy danh sách hóa đơn chờ duyệt. 0 = đang xử lý, 1 = đã duyệt, 2 = đang giao, 3 = đã hoàn thành
   */
  @Override
  @Transactional(readOnly = true)
  public List<HoaDon> getListCanDuyetHoaDonAdmin() {
    try {
      TypedQuery<HoaDon> query =
          entityManager.createQuery(
              "SELECT h FROM HoaDon h WHERE h.tinhtrang = :status", HoaDon.class);

      query.setParameter("status", 0);

      return query.getResultList();

    } catch (Exception e) {
      LOGGER.severe("Error retrieving invoices awaiting approval: " + e.getMessage());
      return List.of();
    }
  }

  /** Lấy danh sách hóa đơn của khách hàng. */
  @Override
  @Transactional(readOnly = true)
  public List<HoaDon> getListHoaDonUser(String hoten) {
    try {

      if (hoten == null || hoten.trim().isEmpty()) {
        LOGGER.warning("Customer name cannot be null or empty");
        return List.of();
      }

      TypedQuery<HoaDon> query =
          entityManager.createQuery(
              "SELECT h FROM HoaDon h WHERE h.tenkhachhang = :customerName", HoaDon.class);

      query.setParameter("customerName", hoten.trim());

      return query.getResultList();

    } catch (Exception e) {
      LOGGER.severe("Error retrieving invoices for customer: " + e.getMessage());
      return List.of();
    }
  }

  /** Lấy danh sách hóa đơn đã duyệt. */
  @Override
  @Transactional(readOnly = true)
  public List<HoaDon> getListHoaDonDaDuyet() {
    try {

      TypedQuery<HoaDon> query =
          entityManager.createQuery(
              "SELECT h FROM HoaDon h WHERE h.tinhtrang = :status", HoaDon.class);

      query.setParameter("status", 1);

      return query.getResultList();

    } catch (Exception e) {
      LOGGER.severe("Error retrieving approved invoices: " + e.getMessage());
      return List.of();
    }
  }

  /** Thêm hóa đơn mới. */
  @Override
  public int addNewHoaDonUser(HoaDon hoaDon) {

    try {

      if (hoaDon == null) {
        LOGGER.warning("Cannot add null invoice");
        return -1;
      }

      entityManager.persist(hoaDon);

      return hoaDon.getMahoadon();

    } catch (Exception e) {

      LOGGER.severe("Error adding new invoice: " + e.getMessage());

      return -1;
    }
  }

  /** Cập nhật hóa đơn. */
  @Override
  public boolean updateHoaDon(HoaDon hoaDon) {

    try {

      if (hoaDon == null) {
        LOGGER.warning("Cannot update null invoice");
        return false;
      }

      entityManager.merge(hoaDon);

      return true;

    } catch (Exception e) {

      LOGGER.severe("Error updating invoice: " + e.getMessage());

      return false;
    }
  }
}

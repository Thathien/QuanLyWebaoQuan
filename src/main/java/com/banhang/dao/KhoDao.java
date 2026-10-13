package com.banhang.dao;

import com.banhang.entity.BienDongKho;
import com.banhang.entity.ChiTietHoaDon;
import com.banhang.entity.ChiTietHoanTien;
import com.banhang.entity.ChiTietPhieuNhap;
import com.banhang.entity.ChiTietSanPham;
import com.banhang.entity.ChiTietTraHang;
import com.banhang.entity.HoaDon;
import com.banhang.entity.HoanTien;
import com.banhang.entity.NhaCungCap;
import com.banhang.entity.PhieuNhap;
import com.banhang.entity.TraHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class KhoDao {
  @PersistenceContext private EntityManager entityManager;

  public ChiTietSanPham findVariantForUpdate(int id) {
    return entityManager.find(ChiTietSanPham.class, id, LockModeType.PESSIMISTIC_WRITE);
  }

  public NhaCungCap findSupplier(long id) {
    return entityManager.find(NhaCungCap.class, id);
  }

  public PhieuNhap findReceiptForUpdate(long id) {
    return entityManager.find(PhieuNhap.class, id, LockModeType.PESSIMISTIC_WRITE);
  }

  public HoaDon findOrderForUpdate(int id) {
    return entityManager.find(HoaDon.class, id, LockModeType.PESSIMISTIC_WRITE);
  }

  public List<ChiTietHoaDon> getOrderLines(int orderId) {
    return entityManager
        .createQuery(
            "SELECT d FROM ChiTietHoaDon d WHERE d.chiTietHoaDonId.mahoadon = :orderId",
            ChiTietHoaDon.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  public ChiTietHoaDon getOrderLine(int orderId, int variantId) {
    return entityManager
        .createQuery(
            """
            SELECT d FROM ChiTietHoaDon d
            WHERE d.chiTietHoaDonId.mahoadon = :orderId
              AND d.chiTietHoaDonId.machitietsanpham = :variantId
            """,
            ChiTietHoaDon.class)
        .setParameter("orderId", orderId)
        .setParameter("variantId", variantId)
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

  public long refundedQuantity(int orderId, int variantId) {
    Long result =
        entityManager
            .createQuery(
                """
                SELECT COALESCE(SUM(r.quantity), 0) FROM ChiTietHoanTien r
                WHERE r.maHoaDon = :orderId AND r.maBienThe = :variantId
                """,
                Long.class)
            .setParameter("orderId", orderId)
            .setParameter("variantId", variantId)
            .getSingleResult();
    return result;
  }

  public java.math.BigDecimal refundedAmount(int orderId, int variantId) {
    return entityManager
        .createQuery(
            """
            SELECT COALESCE(SUM(r.soTien), 0) FROM ChiTietHoanTien r
            WHERE r.maHoaDon = :orderId AND r.maBienThe = :variantId
            """,
            java.math.BigDecimal.class)
        .setParameter("orderId", orderId)
        .setParameter("variantId", variantId)
        .getSingleResult();
  }

  public long returnedQuantity(int orderId, int variantId) {
    return entityManager
        .createQuery(
            """
            SELECT COALESCE(SUM(r.soLuong), 0) FROM ChiTietTraHang r
            WHERE r.maHoaDon = :orderId AND r.maBienThe = :variantId
            """,
            Long.class)
        .setParameter("orderId", orderId)
        .setParameter("variantId", variantId)
        .getSingleResult();
  }

  public boolean hasMovement(int variantId) {
    return entityManager
            .createQuery(
                "SELECT COUNT(m) FROM BienDongKho m WHERE m.bienThe.machitietsanpham = :id",
                Long.class)
            .setParameter("id", variantId)
            .getSingleResult()
        > 0;
  }

  public <T> T persist(T entity) {
    entityManager.persist(entity);
    return entity;
  }

  public <T> T merge(T entity) {
    return entityManager.merge(entity);
  }

  public void flush() {
    entityManager.flush();
  }

  public PhieuNhap getReceipt(long id) {
    return entityManager.find(PhieuNhap.class, id);
  }

  public void deleteDraftReceipt(PhieuNhap receipt) {
    entityManager.remove(entityManager.contains(receipt) ? receipt : entityManager.merge(receipt));
  }

  public List<ChiTietPhieuNhap> getReceiptLines(long receiptId) {
    return entityManager
        .createQuery(
            "SELECT d FROM ChiTietPhieuNhap d WHERE d.phieuNhap.id = :receiptId",
            ChiTietPhieuNhap.class)
        .setParameter("receiptId", receiptId)
        .getResultList();
  }

  public List<PhieuNhap> getReceipts() {
    return entityManager
        .createQuery(
            "SELECT r FROM PhieuNhap r JOIN FETCH r.nhaCungCap ORDER BY r.createdAt DESC, r.id DESC",
            PhieuNhap.class)
        .getResultList();
  }

  public List<ChiTietSanPham> getVariants() {
    return entityManager
        .createQuery(
            """
            SELECT v FROM ChiTietSanPham v
            JOIN FETCH v.sanPham
            JOIN FETCH v.sizeSanPham
            JOIN FETCH v.mauSanPham
            ORDER BY v.sanPham.tensanpham, v.machitietsanpham
            """,
            ChiTietSanPham.class)
        .getResultList();
  }

  public Set<Integer> getVariantIdsWithMovements() {
    return Set.copyOf(
        entityManager
            .createQuery(
                "SELECT DISTINCT m.bienThe.machitietsanpham FROM BienDongKho m", Integer.class)
            .getResultList());
  }

  public void saveMovement(
      ChiTietSanPham variant,
      String type,
      int quantityDelta,
      java.math.BigDecimal unitCost,
      String reference,
      String note) {
    BienDongKho movement = new BienDongKho();
    movement.setBienThe(variant);
    movement.setLoai(type);
    movement.setThayDoiSoLuong(quantityDelta);
    movement.setDonGiaVon(unitCost);
    movement.setMaThamChieu(reference);
    movement.setGhiChu(note);
    persist(movement);
  }

  public List<NhaCungCap> getSuppliers() {
    return entityManager
        .createQuery("SELECT s FROM NhaCungCap s ORDER BY s.ten", NhaCungCap.class)
        .getResultList();
  }

  public List<HoanTien> findRefunds(int orderId) {
    return entityManager
        .createQuery("SELECT r FROM HoanTien r WHERE r.hoaDon.mahoadon = :orderId", HoanTien.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  public List<TraHang> findReturns(int orderId) {
    return entityManager
        .createQuery("SELECT r FROM TraHang r WHERE r.hoaDon.mahoadon = :orderId", TraHang.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  public List<ChiTietHoanTien> getRefundLines(long refundId) {
    return entityManager
        .createQuery(
            "SELECT l FROM ChiTietHoanTien l WHERE l.hoanTien.id = :id", ChiTietHoanTien.class)
        .setParameter("id", refundId)
        .getResultList();
  }

  public List<ChiTietTraHang> getReturnLines(long returnId) {
    return entityManager
        .createQuery(
            "SELECT l FROM ChiTietTraHang l WHERE l.traHang.id = :id", ChiTietTraHang.class)
        .setParameter("id", returnId)
        .getResultList();
  }
}

package com.banhang.dao;

import com.banhang.entity.NhanVien;
import com.banhang.imp.NhanVienImp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class NhanVienDao implements NhanVienImp {

  @PersistenceContext private EntityManager entityManager;

  private NhanVien findByUserAndPassword(String identifier, String matkhau, Integer maChucVu) {
    if (identifier == null || identifier.isBlank() || matkhau == null || matkhau.isEmpty()) {
      return null;
    }
    StringBuilder jpqlBuilder =
        new StringBuilder(
            """
            SELECT n
            FROM NhanVien n
            WHERE (
                LOWER(TRIM(n.tendangnhap)) = LOWER(:identifier)
                OR LOWER(TRIM(n.email)) = LOWER(:identifier)
              )
              AND n.matkhau = :matkhau
              AND COALESCE(n.lock, false) = false
              AND (n.chucVu.machucvu <> 3 OR n.xacthuc = true)
            """);

    if (maChucVu != null) {
      jpqlBuilder.append(" AND n.chucVu.machucvu = :maChucVu");
    }

    TypedQuery<NhanVien> query = entityManager.createQuery(jpqlBuilder.toString(), NhanVien.class);
    query.setParameter("identifier", identifier.trim());
    query.setParameter("matkhau", matkhau);

    if (maChucVu != null) {
      query.setParameter("maChucVu", maChucVu);
    }

    return query.setMaxResults(1).getResultList().stream().findFirst().orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean login(String email, String matkhau) {
    return findByUserAndPassword(email, matkhau, null) != null;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkUser(String email, String matkhau) {
    return findByUserAndPassword(email, matkhau, 3) != null;
  }

  @Override
  @Transactional(readOnly = true)
  public NhanVien getInforby_User_Pass(String email, String matkhau) {

    return findByUserAndPassword(email, matkhau, null);
  }

  @Override
  @Transactional(readOnly = true)
  public NhanVien getCustomerByCredentials(String identifier, String matkhau) {
    return findByUserAndPassword(identifier, matkhau, 3);
  }

  @Override
  @Transactional(readOnly = true)
  public NhanVien getAdminByCredentials(String identifier, String matkhau) {
    return findByUserAndPassword(identifier, matkhau, 1);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkEmail(String email) {

    TypedQuery<NhanVien> query =
        entityManager.createQuery(
            """
            SELECT n
            FROM NhanVien n
            WHERE n.email = :email
            """,
            NhanVien.class);

    query.setParameter("email", email);

    return !query.setMaxResults(1).getResultList().isEmpty();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkUserName(String username) {

    TypedQuery<NhanVien> query =
        entityManager.createQuery(
            """
            SELECT n
            FROM NhanVien n
            WHERE n.tendangnhap = :username
            """,
            NhanVien.class);

    query.setParameter("username", username);

    return !query.setMaxResults(1).getResultList().isEmpty();
  }

  @Override
  @Transactional(readOnly = true)
  public NhanVien getInforbyId(int id) {

    return entityManager.find(NhanVien.class, id);
  }

  @Override
  public int register(NhanVien nv) {

    entityManager.persist(nv);

    return nv.getManhanvien();
  }

  @Transactional(readOnly = true)
  public List<NhanVien> getallTaiKhoan() {

    TypedQuery<NhanVien> query =
        entityManager.createQuery(
            """
            SELECT n
            FROM NhanVien n
            WHERE n.chucVu.machucvu = :maChucVu
            """,
            NhanVien.class);

    query.setParameter("maChucVu", 1);

    return query.getResultList();
  }

  @Transactional(readOnly = true)
  public boolean checkAdmin(String email, String matkhau) {

    return getAdminByCredentials(email, matkhau) != null;
  }

  @Override
  public boolean verifyEmailToken(String token, LocalDateTime currentTime) {
    int updated =
        entityManager
            .createQuery(
                """
                UPDATE NhanVien n
                SET n.xacthuc = true,
                    n.tokenXacThuc = null,
                    n.thoiHanXacThuc = null
                WHERE n.tokenXacThuc = :token
                  AND n.thoiHanXacThuc > :currentTime
                  AND n.chucVu.machucvu = 3
                  AND COALESCE(n.lock, false) = false
                """)
            .setParameter("token", token)
            .setParameter("currentTime", currentTime)
            .executeUpdate();
    return updated == 1;
  }

  @Transactional
  public boolean updateInfor(NhanVien nv) {

    entityManager.merge(nv);

    return true;
  }

  @Transactional
  public boolean changeCustomerPassword(int id, String currentPassword, String newPassword) {
    if (currentPassword == null
        || currentPassword.isEmpty()
        || newPassword == null
        || newPassword.isEmpty()) {
      return false;
    }
    int updated =
        entityManager
            .createQuery(
                """
                UPDATE NhanVien n
                SET n.matkhau = :newPassword
                WHERE n.manhanvien = :id
                  AND n.matkhau = :currentPassword
                  AND n.chucVu.machucvu = 3
                  AND n.xacthuc = true
                  AND COALESCE(n.lock, false) = false
                """)
            .setParameter("id", id)
            .setParameter("currentPassword", currentPassword)
            .setParameter("newPassword", newPassword)
            .executeUpdate();
    return updated == 1;
  }

  @Transactional
  public boolean deleteNhanVien(NhanVien nv) {

    entityManager.remove(entityManager.contains(nv) ? nv : entityManager.merge(nv));

    return true;
  }
}

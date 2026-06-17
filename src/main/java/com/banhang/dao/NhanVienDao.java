package com.banhang.dao;

import com.banhang.entity.NhanVien;
import com.banhang.imp.NhanVienImp;
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
public class NhanVienDao implements NhanVienImp {

  @PersistenceContext private EntityManager entityManager;

  private NhanVien findByUserAndPassword(String email, String matkhau, Integer maChucVu) {

    String jpql =
        """
        SELECT n
        FROM NhanVien n
        WHERE n.tendangnhap = :email
          AND n.matkhau = :matkhau
        """;

    if (maChucVu != null) {
      jpql += " AND n.chucVu.machucvu = :maChucVu";
    }

    TypedQuery<NhanVien> query = entityManager.createQuery(jpql, NhanVien.class);

    query.setParameter("email", email);
    query.setParameter("matkhau", matkhau);

    if (maChucVu != null) {
      query.setParameter("maChucVu", maChucVu);
    }

    List<NhanVien> result = query.setMaxResults(1).getResultList();

    return result.isEmpty() ? null : result.get(0);
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

    return findByUserAndPassword(email, matkhau, 1) != null;
  }

  @Transactional
  public boolean updateInfor(NhanVien nv) {

    entityManager.merge(nv);

    return true;
  }

  @Transactional
  public boolean deleteNhanVien(NhanVien nv) {

    entityManager.remove(entityManager.contains(nv) ? nv : entityManager.merge(nv));

    return true;
  }
}

package com.banhang.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.banhang.entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NhanVienDaoTest {
  @Mock private EntityManager entityManager;
  @Mock private TypedQuery<NhanVien> query;

  private NhanVienDao dao;

  @BeforeEach
  void setUp() {
    dao = new NhanVienDao();
    ReflectionTestUtils.setField(dao, "entityManager", entityManager);
  }

  private void stubEmptyCredentialQuery() {
    when(entityManager.createQuery(anyString(), eq(NhanVien.class))).thenReturn(query);
    when(query.setParameter(anyString(), org.mockito.ArgumentMatchers.any())).thenReturn(query);
    when(query.setMaxResults(1)).thenReturn(query);
    when(query.getResultList()).thenReturn(List.of());
  }

  @Test
  void credentialQueryMatchesEmailOrUsernameAndEnforcesAccountState() {
    stubEmptyCredentialQuery();
    dao.getAdminByCredentials(" admin ", "secret");

    ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
    verify(entityManager).createQuery(queryCaptor.capture(), eq(NhanVien.class));
    String jpql = queryCaptor.getValue();
    assertTrue(jpql.contains("n.tendangnhap"));
    assertTrue(jpql.contains("n.email"));
    assertTrue(jpql.contains("COALESCE(n.lock, false) = false"));
    assertTrue(jpql.contains("n.chucVu.machucvu = :maChucVu"));
    verify(query).setParameter("identifier", "admin");
    verify(query).setParameter("maChucVu", 1);
  }

  @Test
  void credentialQueryRejectsBlankIdentifierOrPasswordWithoutDatabaseQuery() {
    assertTrue(dao.getAdminByCredentials("  ", "secret") == null);
    assertTrue(dao.getCustomerByCredentials("user@example.com", "") == null);
    org.mockito.Mockito.verifyNoInteractions(entityManager);
  }
}

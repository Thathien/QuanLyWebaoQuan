package com.banhang.service;

import com.banhang.dao.NhanVienDao;
import com.banhang.entity.NhanVien;
import com.banhang.imp.NhanVienImp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NhanVienService implements NhanVienImp {
  private final NhanVienDao nhanVienDao;

  public boolean login(String email, String matkhau) {
    return nhanVienDao.login(email, matkhau);
  }

  public boolean checkUser(String email, String matkhau) {
    return nhanVienDao.checkUser(email, matkhau);
  }

  public NhanVien getInforby_User_Pass(String email, String matkhau) {
    return nhanVienDao.getInforby_User_Pass(email, matkhau);
  }

  public NhanVien getCustomerByCredentials(String identifier, String matkhau) {
    return nhanVienDao.getCustomerByCredentials(identifier, matkhau);
  }

  public NhanVien getAdminByCredentials(String identifier, String matkhau) {
    return nhanVienDao.getAdminByCredentials(identifier, matkhau);
  }

  public boolean checkEmail(String email) {
    return nhanVienDao.checkEmail(email);
  }

  public boolean checkUserName(String TDN) {
    return nhanVienDao.checkUserName(TDN);
  }

  public NhanVien getInforbyId(int id) {
    return nhanVienDao.getInforbyId(id);
  }

  public int register(NhanVien nv) {
    return nhanVienDao.register(nv);
  }

  public List<NhanVien> getallTaiKhoan() {
    return nhanVienDao.getallTaiKhoan();
  }

  public boolean checkAdmin(String email, String matkhau) {
    return nhanVienDao.checkAdmin(email, matkhau);
  }

  public boolean verifyEmailToken(String token, LocalDateTime currentTime) {
    return nhanVienDao.verifyEmailToken(token, currentTime);
  }

  public boolean updateInfor(NhanVien nv) {
    return nhanVienDao.updateInfor(nv);
  }

  public boolean changeCustomerPassword(int id, String currentPassword, String newPassword) {
    return nhanVienDao.changeCustomerPassword(id, currentPassword, newPassword);
  }

  public boolean deleteNhanVien(NhanVien nv) {
    return nhanVienDao.deleteNhanVien(nv);
  }
}

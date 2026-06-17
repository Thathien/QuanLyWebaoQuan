package com.banhang.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lớp đại diện cho thông tin tài khoản đăng nhập của nhân viên. Chứa các thông tin cơ bản về nhân
 * viên bao gồm mã nhân viên, tên đăng nhập, mật khẩu, họ tên và mã chức vụ.
 *
 * @author System
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanLogin {

  /** Mã nhân viên - định danh duy nhất cho mỗi nhân viên */
  private int manhanvien;

  /** Tên đăng nhập - tên người dùng để đăng nhập vào hệ thống */
  private String tendangnhap;

  /** Mật khẩu - được lưu trữ (nên được mã hóa khi lưu vào cơ sở dữ liệu) */
  private String matkhau;

  /** Họ tên - tên đầy đủ của nhân viên */
  private String hoten;

  /** Mã chức vụ - định danh chức vụ của nhân viên trong tổ chức */
  private int machucvu;
}

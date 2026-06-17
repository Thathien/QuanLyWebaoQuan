package com.banhang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity đại diện cho thông tin nhân viên. Chứa các thông tin cá nhân, liên hệ và vai trò của nhân
 * viên trong hệ thống.
 *
 * @author System
 * @version 1.0
 */
@Entity(name = "NhanVien")
@Table(name = "NhanVien")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"chucVu"})
@EqualsAndHashCode(exclude = {"chucVu"})
public class NhanVien implements Serializable {

  /** Mã nhân viên - định danh duy nhất */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "manhanvien")
  private Integer manhanvien;

  /** Họ tên của nhân viên */
  @Column(name = "hoten", columnDefinition = "nvarchar(100)")
  private String hoten;

  /** Địa chỉ của nhân viên */
  @Column(name = "diachi", columnDefinition = "nvarchar(200)")
  private String diachi;

  /** Giới tính của nhân viên */
  @Column(name = "gioitinh", columnDefinition = "bit")
  private Boolean gioitinh;

  /** Chứng minh nhân dân */
  @Column(name = "cmnd", columnDefinition = "nvarchar(20)")
  private String cmnd;

  /** Trạng thái khóa tài khoản */
  @Column(name = "lock", columnDefinition = "bit")
  private Boolean lock;

  /** Lý do khóa tài khoản */
  @Column(name = "lydokhoa", columnDefinition = "nvarchar(500)")
  private String lydokhoa;

  /** Trạng thái xác thực */
  @Column(name = "xacthuc", columnDefinition = "bit")
  private Boolean xacthuc;

  /** Chức vụ của nhân viên */
  @ManyToOne
  @JoinColumn(name = "machucvu")
  private ChucVu chucVu;

  /** Email của nhân viên */
  @Column(columnDefinition = "nvarchar(100)")
  private String email;

  /** Tên đăng nhập của nhân viên */
  @Column(columnDefinition = "nvarchar(50)")
  private String tendangnhap;

  /** Mật khẩu của nhân viên (nên được mã hóa) */
  @Column(columnDefinition = "nvarchar(100)")
  private String matkhau;
}

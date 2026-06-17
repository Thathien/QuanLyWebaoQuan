package com.banhang.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;

@Entity
@Table(name = "NhanVien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "chucVu")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhanVien implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /** Mã nhân viên */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "manhanvien")
  @EqualsAndHashCode.Include
  private Integer manhanvien;

  /** Họ tên */
  @Column(name = "hoten", length = 100)
  private String hoten;

  /** Địa chỉ */
  @Column(name = "diachi", length = 200)
  private String diachi;

  /** Giới tính */
  @Column(name = "gioitinh")
  private Boolean gioitinh;

  /** CMND/CCCD */
  @Column(name = "cmnd", length = 20)
  private String cmnd;

  /** Trạng thái khóa */
  @Column(name = "lock")
  private Boolean lock;

  /** Lý do khóa */
  @Column(name = "lydokhoa", length = 500)
  private String lydokhoa;

  /** Xác thực */
  @Column(name = "xacthuc")
  private Boolean xacthuc;

  /** Chức vụ */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "machucvu")
  private ChucVu chucVu;

  /** Email */
  @Column(name = "email", length = 100)
  private String email;

  /** Tên đăng nhập */
  @Column(name = "tendangnhap", length = 50)
  private String tendangnhap;

  /** Mật khẩu */
  @Column(name = "matkhau", length = 100)
  private String matkhau;
}

package com.banhang.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity đại diện cho danh mục sản phẩm. Dùng để phân loại các sản phẩm trong hệ thống.
 *
 * @author System
 * @version 1.0
 */
@Entity(name = "DanhMucSanPham")
@Table(name = "DanhMucSanPham")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"danhsachsanpham"})
@EqualsAndHashCode(exclude = {"danhsachsanpham"})
public class DanhMucSanPham implements Serializable {

  /** Mã danh mục sản phẩm - định danh duy nhất */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "madanhmucsanpham")
  private Integer madanhmucsanpham;

  /** Tên danh mục */
  @Column(name = "tendanhmuc", columnDefinition = "nvarchar(100)")
  private String tendanhmuc;

  /** Hình ảnh danh mục */
  @Column(name = "hinhdanhmuc", columnDefinition = "nvarchar(200)")
  private String hinhdanhmuc;

  /** Trạng thái ẩn/hiện */
  @Column(name = "hiden", columnDefinition = "bit")
  private Boolean hiden;

  /** Danh sách sản phẩm trong danh mục */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "madanhmucsanpham")
  private Set<SanPham> danhsachsanpham;
}

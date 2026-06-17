package com.banhang.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity đại diện cho thông tin sản phẩm. Chứa các thông tin chi tiết về sản phẩm bán trong hệ
 * thống.
 *
 * @author System
 * @version 1.0
 */
@Entity(name = "SanPham")
@Table(name = "SanPham")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"chiTietSanPham", "khuyenMai", "danhMucSanPham"})
@EqualsAndHashCode(exclude = {"chiTietSanPham", "khuyenMai", "danhMucSanPham"})
public class SanPham implements Serializable {
  /** Mã sản phẩm - định danh duy nhất */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "masanpham")
  private Integer masanpham;

  /** Danh mục sản phẩm */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "madanhmucsanpham")
  private DanhMucSanPham danhMucSanPham;

  /** Tên sản phẩm */
  @Column(name = "tensanpham", columnDefinition = "nvarchar(100)")
  private String tensanpham;

  /** Giá tiền sản phẩm */
  @Column(name = "giatien", columnDefinition = "nvarchar(20)")
  private String giatien;

  /** Mô tả sản phẩm */
  @Column(name = "mota", columnDefinition = "nvarchar(500)")
  private String mota;

  /** Hình ảnh sản phẩm */
  @Column(name = "hinhsanpham", columnDefinition = "nvarchar(200)")
  private String hinhsanpham;

  /** Trạng thái ẩn/hiện */
  @Column(name = "hiden", columnDefinition = "bit")
  private Boolean hiden;

  /** Đối tượng sử dụng */
  @Column(name = "doituong", columnDefinition = "nvarchar(100)")
  private String doituong;

  /** Chi tiết sản phẩm */
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sanPham")
  private Set<ChiTietSanPham> chiTietSanPham = new HashSet<>();

  /** Danh sách khuyến mại áp dụng */
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "CHITIETKHUYENMAI",
      joinColumns = {@JoinColumn(name = "masanpham", referencedColumnName = "masanpham")},
      inverseJoinColumns = {
        @JoinColumn(name = "makhuyenmai", referencedColumnName = "makhuyenmai")
      })
  private Set<KhuyenMai> khuyenMai = new HashSet<>();
}

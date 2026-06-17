package com.banhang.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "KhuyenMai")
@Table(name = "KhuyenMai")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "makhuyenmai")
  int makhuyenmai;

  @Column(name = "giagiam")
  float giagiam;

  @Column(name = "tenkhuyenmai", columnDefinition = "nvarchar(50)")
  String tenkhuyenmai;

  @Column(name = "thoigianbatdau", columnDefinition = "nvarchar(50)")
  String thoigianbatdau;

  @Column(name = "thoigianketthuc", columnDefinition = "nvarchar(50)")
  String thoigianketthuc;

  @Column(name = "mota", columnDefinition = "nvarchar(50)")
  String mota;

  @Column(name = "hinhkhuyenmai", columnDefinition = "nvarchar(50)")
  String hinhkhuyenmai;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "CHITIETKHUYENMAI",
      joinColumns = {@JoinColumn(name = "makhuyenmai", referencedColumnName = "makhuyenmai")},
      inverseJoinColumns = {@JoinColumn(name = "masanpham", referencedColumnName = "masanpham")})
  Set<SanPham> sanPham;
}

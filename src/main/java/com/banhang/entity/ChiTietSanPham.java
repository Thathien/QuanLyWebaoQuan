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
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ChiTietSanPham")
@Table(name = "ChiTietSanPham")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChiTietSanPham implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "machitietsanpham")
  int machitietsanpham;

  @ManyToOne
  @JoinColumn(name = "masanpham")
  SanPham sanPham;

  @ManyToOne
  @JoinColumn(name = "masize")
  SizeSanPham sizeSanPham;

  @ManyToOne
  @JoinColumn(name = "mamau")
  MauSanPham mauSanPham;

  int soluong;

  String ngaynhap;

  @Column(name = "gia_von_binh_quan", precision = 19, scale = 2)
  BigDecimal giaVonBinhQuan;

  @Column(name = "ton_toi_thieu", nullable = false)
  int tonToiThieu;
}

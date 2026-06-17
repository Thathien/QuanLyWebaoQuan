package com.banhang.entity;

import jakarta.persistence.CascadeType;
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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "masanpham")
  SanPham sanPham;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "masize")
  SizeSanPham sizeSanPham;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "mamau")
  MauSanPham mauSanPham;

  int soluong;

  String ngaynhap;
}

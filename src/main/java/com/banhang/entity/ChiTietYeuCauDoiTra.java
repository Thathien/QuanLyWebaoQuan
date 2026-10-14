package com.banhang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ChiTietYeuCauDoiTra")
@Table(name = "ChiTietYeuCauDoiTra")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietYeuCauDoiTra {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "yeu_cau_id", nullable = false)
  private YeuCauDoiTra yeuCau;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "bien_the_id", nullable = false)
  private ChiTietSanPham bienThe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bien_the_thay_the_id")
  private ChiTietSanPham bienTheThayThe;

  @Column(name = "so_luong", nullable = false)
  private int soLuong;
}

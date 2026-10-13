package com.banhang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ChiTietPhieuNhap")
@Table(
    name = "ChiTietPhieuNhap",
    indexes = @Index(name = "IX_ChiTietPhieuNhap_Variant", columnList = "variant_id"))
@Getter
@Setter
@NoArgsConstructor
public class ChiTietPhieuNhap {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "receipt_id", nullable = false)
  private PhieuNhap phieuNhap;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "variant_id", nullable = false)
  private ChiTietSanPham bienThe;

  @Column(name = "quantity", nullable = false)
  private int soLuong;

  @Column(name = "unit_cost", nullable = false, precision = 19, scale = 2)
  private BigDecimal donGiaVon;
}

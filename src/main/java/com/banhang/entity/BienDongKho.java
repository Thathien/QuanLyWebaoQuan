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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "BienDongKho")
@Table(
    name = "BienDongKho",
    indexes = {
      @Index(name = "IX_BienDongKho_Variant_Time", columnList = "variant_id, occurred_at"),
      @Index(name = "IX_BienDongKho_Type_Time", columnList = "movement_type, occurred_at")
    })
@Getter
@Setter
@NoArgsConstructor
public class BienDongKho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "variant_id", nullable = false)
  private ChiTietSanPham bienThe;

  @Column(name = "movement_type", nullable = false, length = 32)
  private String loai;

  @Column(name = "quantity_delta", nullable = false)
  private int thayDoiSoLuong;

  @Column(name = "unit_cost", precision = 19, scale = 2)
  private BigDecimal donGiaVon;

  @Column(name = "reference_id", length = 100)
  private String maThamChieu;

  @Column(name = "occurred_at", nullable = false)
  private LocalDateTime thoiDiem = LocalDateTime.now();

  @Column(name = "note", length = 255)
  private String ghiChu;
}

package com.banhang.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "YeuCauDoiTra")
@Table(
    name = "YeuCauDoiTra",
    indexes = @Index(name = "IX_YeuCauDoiTra_Order_Status", columnList = "mahoadon, trang_thai"))
@Getter
@Setter
@NoArgsConstructor
public class YeuCauDoiTra {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mahoadon", nullable = false)
  private HoaDon hoaDon;

  @Enumerated(EnumType.STRING)
  @Column(name = "loai_yeu_cau", nullable = false, length = 10)
  private LoaiYeuCauDoiTra loaiYeuCau;

  @Enumerated(EnumType.STRING)
  @Column(name = "trang_thai", nullable = false, length = 20)
  private TrangThaiYeuCauDoiTra trangThai = TrangThaiYeuCauDoiTra.CHO_XU_LY;

  @Column(name = "ly_do", nullable = false, length = 255)
  private String lyDo;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @OneToMany(mappedBy = "yeuCau", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChiTietYeuCauDoiTra> chiTiet = new ArrayList<>();
}

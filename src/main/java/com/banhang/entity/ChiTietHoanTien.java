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

@Entity(name = "ChiTietHoanTien")
@Table(
    name = "ChiTietHoanTien",
    indexes =
        @Index(name = "IX_ChiTietHoanTien_InvoiceLine", columnList = "invoice_id, variant_id"))
@Getter
@Setter
@NoArgsConstructor
public class ChiTietHoanTien {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "refund_id", nullable = false)
  private HoanTien hoanTien;

  @Column(name = "invoice_id", nullable = false)
  private int maHoaDon;

  @Column(name = "variant_id", nullable = false)
  private int maBienThe;

  @Column(name = "amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal soTien;

  @Column(name = "quantity", nullable = false)
  private int soLuong;
}

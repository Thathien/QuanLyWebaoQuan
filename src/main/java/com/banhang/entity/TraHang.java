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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TraHang")
@Table(
    name = "TraHang",
    indexes = @Index(name = "IX_TraHang_Invoice_Time", columnList = "invoice_id, received_at"))
@Getter
@Setter
@NoArgsConstructor
public class TraHang {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "invoice_id", nullable = false)
  private HoaDon hoaDon;

  @Column(name = "received_at", nullable = false)
  private LocalDateTime thoiDiemNhan = LocalDateTime.now();

  @Column(name = "reason", nullable = false, length = 255)
  private String lyDo;
}

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

@Entity(name = "PhieuNhap")
@Table(
    name = "PhieuNhap",
    indexes = @Index(name = "IX_PhieuNhap_Status_ConfirmedAt", columnList = "status, confirmed_at"))
@Getter
@Setter
@NoArgsConstructor
public class PhieuNhap {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "supplier_id", nullable = false)
  private NhaCungCap nhaCungCap;

  @Column(name = "status", nullable = false, length = 20)
  private String status = "DRAFT";

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "confirmed_at")
  private LocalDateTime confirmedAt;
}

package com.banhang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "NhaCungCap")
@Table(name = "NhaCungCap", indexes = @Index(name = "IX_NhaCungCap_Ten", columnList = "ten"))
@Getter
@Setter
@NoArgsConstructor
public class NhaCungCap {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ten", nullable = false, length = 150)
  private String ten;

  @Column(name = "email", length = 255)
  private String email;

  @Column(name = "so_dien_thoai", length = 50)
  private String soDienThoai;

  @Column(name = "dia_chi", length = 255)
  private String diaChi;
}

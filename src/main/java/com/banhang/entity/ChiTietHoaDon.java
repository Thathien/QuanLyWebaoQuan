package com.banhang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ChiTietHoaDon")
@Table(name = "ChiTietHoaDon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietHoaDon implements Serializable {
  @EmbeddedId ChiTietHoaDonId chiTietHoaDonId;

  @Column(name = "soluong", columnDefinition = "int")
  private int soluong;

  @Column(name = "giatien", columnDefinition = "nvarchar(50)")
  private String giatien;

  @ManyToOne
  @JoinColumn(name = "mahoadon", insertable = false, updatable = false)
  private HoaDon hoaDon;
}

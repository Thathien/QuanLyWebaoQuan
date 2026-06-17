package com.banhang.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "SizeSanPham")
@Table(name = "SizeSanPham")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SizeSanPham implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "masize")
  int masize;

  @Column(name = "size", columnDefinition = "nvarchar(50)")
  String size;

  //	@OneToOne(mappedBy = "sizeSanPham")
  @OneToMany(mappedBy = "sizeSanPham", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  //	private ChiTietSanPham chiTietSanPham;
  private Set<ChiTietSanPham> chiTietSanPhams = new HashSet<ChiTietSanPham>();
}

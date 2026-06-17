package com.banhang.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuc_vu")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "nhanViens")
public class ChucVu implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(name = "machucvu")
  private Integer machucvu;

  @Column(name = "tenchucvu", nullable = false, length = 100)
  private String tenchucvu;

  @OneToMany(mappedBy = "chucVu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  private Set<NhanVien> nhanViens = new HashSet<>();
}

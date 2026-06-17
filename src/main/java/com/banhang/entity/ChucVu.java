package com.banhang.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity đại diện cho chức vụ trong tổ chức. Dùng để gán vai trò và quyền hạn cho nhân viên.
 *
 * @author System
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ChucVu")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChucVu implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Integer machucvu;

  private String tenchucvu;

  @OneToMany(mappedBy = "chucVu")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<NhanVien> nhanViens = new HashSet<>();
}

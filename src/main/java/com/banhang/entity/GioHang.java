package com.banhang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GioHang {
  int machitietsanpham;
  int masp;
  int masize;
  int mamau;
  String tensp;
  String giatien;
  String tenmau;
  String tensize;
  int soluong;
}

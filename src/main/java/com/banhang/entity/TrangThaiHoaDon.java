package com.banhang.entity;

import java.util.Arrays;

public enum TrangThaiHoaDon {
  CHO_XU_LY(0),
  DA_DUYET(1),
  DANG_GIAO(2),
  HOAN_THANH(3),
  DA_HUY(4);

  private final int code;

  TrangThaiHoaDon(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static TrangThaiHoaDon fromCode(int code) {
    return Arrays.stream(values())
        .filter(status -> status.code == code)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Trạng thái hóa đơn không hợp lệ."));
  }
}

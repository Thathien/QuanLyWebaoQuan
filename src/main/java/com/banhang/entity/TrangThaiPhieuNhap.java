package com.banhang.entity;

public enum TrangThaiPhieuNhap {
  NHAP("DRAFT"),
  DA_XAC_NHAN("CONFIRMED"),
  DA_HUY("CANCELLED");

  private final String code;

  TrangThaiPhieuNhap(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}

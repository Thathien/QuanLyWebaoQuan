package com.banhang.entity;

public enum LoaiBienDongKho {
  TON_DAU_KY("OPENING_BALANCE"),
  NHAP_HANG("RECEIPT"),
  BAN_HANG("SALE"),
  HUY_DON("ORDER_CANCELLATION"),
  KHACH_TRA_HANG("CUSTOMER_RETURN"),
  DIEU_CHINH("ADJUSTMENT");

  private final String code;

  LoaiBienDongKho(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}

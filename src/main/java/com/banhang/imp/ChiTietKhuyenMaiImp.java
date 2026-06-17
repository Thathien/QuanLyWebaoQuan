package com.banhang.imp;

import com.banhang.entity.ChiTietKhuyenMai;
import java.util.List;

public interface ChiTietKhuyenMaiImp {
  public List<ChiTietKhuyenMai> getAllChiTietKhuyenMai();

  public int addNewKhuyenMaiChoSP(ChiTietKhuyenMai ctkm);

  public boolean deletKhuyenMaiSP(ChiTietKhuyenMai ctkm);
}

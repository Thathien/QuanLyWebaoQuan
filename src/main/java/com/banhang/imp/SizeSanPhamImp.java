package com.banhang.imp;

import com.banhang.entity.SizeSanPham;
import java.util.List;

public interface SizeSanPhamImp {
  public List<SizeSanPham> getAllSizeSanPham();

  public int addSizeSanPham(SizeSanPham ssp);

  public boolean updateSizeSanPham(SizeSanPham ssp);

  public boolean deleteSizeSanPham(SizeSanPham ssp);
}

package com.banhang.imp;

import com.banhang.entity.DanhMucSanPham;
import java.util.List;

public interface DanhMucSanPhamImp {
  public List<DanhMucSanPham> getAllDanhMucSanPham();

  public int addDanhMucSanPham(DanhMucSanPham dm);

  public boolean updateDanhMucSanPham(DanhMucSanPham dm);

  public boolean deleteDanhMucSanPham(DanhMucSanPham dm);

  public boolean checkNameDanhMucBeforeAdd(String name);
}

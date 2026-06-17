package com.banhang.imp;

import com.banhang.entity.KhuyenMai;
import java.util.List;

public interface KhuyenMaiImp {
  public List<KhuyenMai> getAllKhuyenMai();

  public int addKhuyenMai(KhuyenMai km);

  public boolean updateKhuyenMai(KhuyenMai km);

  public boolean deleteKhuyenMai(KhuyenMai km);

  public boolean checkNameKhuyenMai(String name);
}

package com.banhang.imp;

import com.banhang.entity.HoaDon;
import java.util.List;

public interface HoaDonImp {
  public List<HoaDon> getListCanDuyetHoaDonAdmin();

  public List<HoaDon> getListHoaDonUser(String hoten);

  public List<HoaDon> getListHoaDonDaDuyet();

  public int addNewHoaDonUser(HoaDon hoaDon);

  public boolean updateHoaDon(HoaDon hoaDon);
}

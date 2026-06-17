package com.banhang.service;

import com.banhang.dao.HoaDonDao;
import com.banhang.entity.HoaDon;
import com.banhang.imp.HoaDonImp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoaDonService implements HoaDonImp {
  private final HoaDonDao hoaDonDao;

  public List<HoaDon> getListCanDuyetHoaDonAdmin() {
    return hoaDonDao.getListCanDuyetHoaDonAdmin();
  }

  public List<HoaDon> getListHoaDonUser(String hoten) {
    return hoaDonDao.getListHoaDonUser(hoten);
  }

  public List<HoaDon> getListHoaDonDaDuyet() {
    return hoaDonDao.getListHoaDonDaDuyet();
  }

  public int addNewHoaDonUser(HoaDon hoaDon) {
    return hoaDonDao.addNewHoaDonUser(hoaDon);
  }

  public boolean updateHoaDon(HoaDon hoaDon) {
    return hoaDonDao.updateHoaDon(hoaDon);
  }
}
